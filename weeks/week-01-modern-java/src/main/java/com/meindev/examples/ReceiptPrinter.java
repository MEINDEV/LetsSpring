package com.meindev.examples;

import java.util.List;

public class ReceiptPrinter {

    public record Line(String item,int qty,float pricePaise){}

    public static String print(String customer,List<Line> items){

        var sb = new StringBuilder();
        float total = 0;

        sb.append("Customer: ").append(customer).append("\n");
        sb.repeat("-", 50);
        sb.append("\n");

        for(var item : items){
            String itemName = item.item();
            int itemQty = item.qty();
            float totalItemPrice = item.pricePaise()*itemQty/100;
            sb.append("%-20s %-3d %8.2f\n".formatted(itemName,itemQty,totalItemPrice));
            total += totalItemPrice;
        }

        sb.repeat("-",50);
        sb.append("\n");
        sb.append("Total: ").append("%8.2f".formatted(total));

        return sb.toString();
    }


    public static void main(String[] args) {
        System.out.println(ReceiptPrinter.print(
                "Sneha",
                List.of(
                        new Line("Lemon Team", 3, 2_000),
                        new Line("Paneer Makhni", 2,42_500),
                        new Line("Jira Rice",2,50_000)
                )
        ));
    }
}
