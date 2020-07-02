package com.lucca.fileSorter.service;

import com.lucca.fileSorter.model.Sale;
import com.lucca.fileSorter.model.Salesman;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SaleService {

    public List<Sale> getBestSale(List<Sale> salesList) {
        List<Sale> bestSale = new ArrayList<>();
        salesList.sort(Comparator.comparingDouble(Sale::getSaleValue).reversed());

        for (Sale sale: salesList) {
            if(bestSale.isEmpty()){
                bestSale.add(sale);
            } else if(Double.compare(bestSale.get(0).getSaleValue(), sale.getSaleValue()) == 0){
                bestSale.add(sale);
            }
        }
        return bestSale;
    }

    public List<Salesman> getWorseSalesman(List<Sale> saleList){
        Map<Salesman,Double> salesMap = new HashMap();
        List<Salesman> worstSalesman = new ArrayList<>();
        for (Sale sale: saleList) {
            if(salesMap.containsKey(sale.getSalesman())) {
                Double totalValue =  salesMap.get(sale.getSalesman()) + sale.getSaleValue();
                salesMap.replace(sale.getSalesman(),totalValue);
            } else {
                salesMap.put(sale.getSalesman(),sale.getSaleValue());
            }
        }

        Double worstSale = Collections.min(salesMap.values());

        for (Map.Entry<Salesman,Double> entry : salesMap.entrySet()) {
            if (Double.compare(entry.getValue(), worstSale ) == 0) {
                worstSalesman.add(entry.getKey());
            }
        }
        return worstSalesman;
    }
}
