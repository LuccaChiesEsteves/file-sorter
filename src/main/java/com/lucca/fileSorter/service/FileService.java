package com.lucca.fileSorter.service;

import com.lucca.fileSorter.dto.OutputFile;
import com.lucca.fileSorter.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private SaleService saleService;

    @Autowired
    private Environment env;

    private List<ContentFile> listContentFiles(String path){

        List<ContentFile> contentFileList = new ArrayList<>();

        if (!StringUtils.isEmpty(path)){

            try{
                File filesPath = new File(path);
                File[] filesList = filesPath.listFiles();

                if(filesList != null) {

                    for (File file : filesList){

                        if(file.getName().endsWith(".dat")){

                            ContentFile contentFile = new ContentFile();
                            StringBuilder content = new StringBuilder();

                            contentFile.setName(file.getName());
                            contentFile.setFullPath(file.getCanonicalPath());
                            Files.readAllLines(Paths.get(file.getPath())).forEach(line -> content.append(line + "\n"));

                            contentFile.setContent(content);
                            contentFileList.add(contentFile);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getStackTrace().toString());
            }
        }
        return contentFileList;
    }

    public void processFiles() throws IOException {

        List<ContentFile> contentFileList = this.listContentFiles(System.getProperty("user.home") + env.getProperty("file.input.directory"));
        int fileCounter = 0;

        if (!contentFileList.isEmpty()){

            for (ContentFile contentFile : contentFileList){
                 OutputFile outputFile = sortContentFile(contentFile);
                 saveOutputFile(outputFile);
                 Files.delete(Paths.get(contentFile.getFullPath()));
                 fileCounter ++;
            }

            System.out.println("Rotina finalizada " + fileCounter + " arquivos processados,");
        } else {
            System.out.println("Rotina finalizada nenhum arquivo processado.");
        }
    }

    private OutputFile sortContentFile(ContentFile contentFile) {

        List<Salesman> salesmanList = new ArrayList<>();
        List<Client> clientList = new ArrayList<>();
        List<Sale> saleList = new ArrayList<>();
        String[] lines = contentFile.getContent().toString().split("\n");

        for (String line : lines) {

            switch (line.substring(0,line.indexOf("ç"))){

                case "001":
                    salesmanList.add(splitSalesmanLine(line));
                    break;

                case "002":
                    clientList.add(splitClientLine(line));
                    break;

                case "003":
                    saleList.add(splitSaleLine(line));
                    break;
            }
        }

        return generateOutputFile(salesmanList,clientList,saleList,contentFile.getName());
    }

    private Salesman splitSalesmanLine (String line){
        String[] parts = line.split("ç");
        return new Salesman(parts[1],parts[2], Float.parseFloat(parts[3]));
    }

    private Client splitClientLine (String line){
        String[] parts = line.split("ç");
        return new Client(parts[1],parts[2],parts[3]);
    }

    private Sale splitSaleLine(String line){
        String[] parts = line.split("ç");
        List<Item> itemsList = splitItems(parts[2]);
        return new Sale(Integer.parseInt(parts[1]),itemsList, new Salesman(parts[3]));
    }

    private List<Item> splitItems(String items) {
        List<Item> itemList = new ArrayList<>();
        items = items.replaceAll("[\\[\\]]", "");

        String[] rawItems = items.split(",");

        for (String rawItem : rawItems){
            String[] parts = rawItem.split("-");
            Item item = new Item(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]),Double.parseDouble(parts[2]));

            itemList.add(item);
        }

        return itemList;
    }


    private OutputFile generateOutputFile(List<Salesman> salesmanList, List<Client> clientList, List<Sale> salesList, String fileName) {
        String content = generateContent(salesmanList.size(),clientList.size(), saleService.getBestSale(salesList), saleService.getWorseSalesman(salesList));

        return new OutputFile(System.getProperty("user.home") + env.getProperty("file.output.directory") + fileName, content);
    }

    private void saveOutputFile (OutputFile outputFile) throws IOException {
        StringBuilder fileName = new StringBuilder();
        fileName.append(outputFile.getName());
        fileName.replace((fileName.length() -3),fileName.length(), "done.dat");

        final File file = new File(fileName.toString());
        file.setWritable(true);
        final FileWriter out = new FileWriter(file);
        final BufferedWriter bw = new BufferedWriter(out);
        bw.write(outputFile.getContent());
        bw.flush();
        bw.close();
    }

    private String generateContent(int salesmanCount, int clientCount, List<Sale> bestSaleList, List<Salesman> worstSalesmanList) {
        StringBuilder content = new StringBuilder();
        StringBuilder worstSalesman = new StringBuilder();
        StringBuilder bestSales = new StringBuilder();

        worstSalesmanList.forEach(salesman -> worstSalesman.append(salesman.getName() + " ")) ;
        bestSaleList.forEach(sale -> bestSales.append(sale.getId() + " "));

        content.append("Quantidade de clientes: " + clientCount
                        + "\nQuantidade de vendedores: " + salesmanCount
                        + "\nID da(s) venda(s) mais cara(s): " + bestSales
                        + "\nPior(es) vendedor(es): " + worstSalesman);

        return content.toString();
    }
}