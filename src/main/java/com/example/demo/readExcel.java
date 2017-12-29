package com.example.demo;

/**
 * @author ludonglue
 * @date 2017/12/27
 */

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * 读取Excel
 *
 * @author zengwendong
 */
public class readExcel {
    public static void main(String[] args) throws Exception {
        String configPath = "C:\\Users\\ludonglue\\Desktop\\config.txt";
        String inputFilePath1 = "C:\\Users\\ludonglue\\Desktop\\table1.xls";
        String inputFilePath2 = "C:\\Users\\ludonglue\\Desktop\\table2.xls";
        String outputFilePath = "C:\\Users\\ludonglue\\Desktop\\table3.xls";
        Map<String,List<String>> mapper = readMapperTxt(configPath);
        if(mapper.size()==0){
            System.out.println("读取配置文件时候发生错误");
            return;
        }
        
        ArrayList<Atable> aList = readSheetA(inputFilePath1);
        ArrayList<Btable> bList = readSheetB(inputFilePath2);
        ArrayList<Atable> resultList = cal(aList,bList,mapper);
        output(outputFilePath,resultList);



    }

    private static void output(String outputFilePath,ArrayList<Atable> resultList) {
        // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
        FileOutputStream fos =  null;
        Workbook workbook = new HSSFWorkbook();
        try {
            fos = new FileOutputStream(new File(outputFilePath));
            Sheet result = workbook.getSheet("result");
            if (result == null) {
                result = workbook.createSheet("result");
            }

            for (int i = 0; i < resultList.size(); i++) {
                Row rowResult = result.createRow(i);
                Cell cell1 = rowResult.createCell(0);
                if (resultList.get(i).getName() != null) {
                    cell1.setCellValue(resultList.get(i).getName());
                }
                if (resultList.get(i).getWeight() != null) {
                    Cell cell2 = rowResult.createCell(1);
                    cell2.setCellValue(resultList.get(i).getWeight());
                }

            }

            //写入数据
            workbook.write(fos);
            fos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(fos!=null){

                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static ArrayList<Atable> cal(ArrayList<Atable> aList, ArrayList<Btable> bList, Map<String,List<String>> mapper) {
        for (int i = 0; i < aList.size(); i++) {
            System.out.print(aList.get(i));
        }
        System.out.println("");
        for (int i = 0; i < bList.size(); i++) {
            System.out.print(bList.get(i));
        }
        System.out.println("");
        ArrayList<Atable> resultList = new ArrayList<>();
        for(int i=0;i<bList.size();i++){
            Atable result = new Atable();
            Btable btable = bList.get(i);
            System.out.println("key:"+btable.getName());
            List<String> values = mapper.get(btable.getName());
            System.out.println("===="+values);
            Double weight = 0.0;
            for(int j=0;j<aList.size();j++){
                Atable atable = aList.get(j);
                System.out.println("==="+atable);
                for(int k=0;k<values.size();k++){
                    System.out.println("值:"+values.get(k)+"，table值："+atable.getName());
                    if(values.get(k).equals(atable.getName()) && atable.getWeight()!=null){
                        weight += atable.getWeight();
                        System.out.println("重量："+weight);
                    }
                }
                result.setName(btable.getName());
                result.setWeight(weight);
            }
            resultList.add(result);
        }
        return resultList;
    }

    public static Map<String,List<String>> readMapperTxt(String path) {
        //两个表的映射关系，多对一
        Map<String, List<String>> mapper = new HashMap<>();
        BufferedReader br = null;
        InputStreamReader reader = null;
        //读txt
        try {
            File filename = new File(path);
            reader = new InputStreamReader(
                    new FileInputStream(filename), "gbk"); // 建立一个输入流对象reader
            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] lineStr = line.split(",");
                String multi = lineStr[0];
                String key = lineStr[1];
                List<String> tmpList = mapper.get(key);
                if (tmpList == null) {
                    tmpList = new ArrayList<>();
                }
                tmpList.add(multi);
                mapper.put(key, tmpList);
            }
            System.out.println("mapper:" + mapper);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {

                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {

                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mapper;
    }
    
    public static ArrayList<Atable> readSheetA(String path){
        //读excel
        //创建输入流
        FileInputStream fis = null;
        HSSFWorkbook workbook = null;
        ArrayList<Atable> sheetAList = new ArrayList<>();
        try {
            fis = new FileInputStream(new File(path));
            //通过构造函数传参
            workbook = new HSSFWorkbook(fis);
            //获取工作表
            HSSFSheet sheetA = workbook.getSheetAt(0);
            Integer rowANum = sheetA.getLastRowNum() + 1;
            //遍历转对象，不包括首行
            for (int i = 1; i < rowANum; i++) {
                HSSFRow rowA = sheetA.getRow(i);
                if (rowA == null) {
                    continue;
                }
                Atable tableA = new Atable();
                if (rowA.getCell(0) != null) {
                    tableA.setName(rowA.getCell(0).getStringCellValue());
                }
                if (rowA.getCell(1) != null) {
                    tableA.setWeight(rowA.getCell(1).getNumericCellValue());
                }
                sheetAList.add(tableA);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(workbook != null){
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sheetAList;
    }

    public static ArrayList<Btable> readSheetB(String path) {
        //读excel
        //创建输入流
        FileInputStream fis = null;
        HSSFWorkbook workbook = null;
        ArrayList<Btable> sheetBList = new ArrayList<>();
        try {
            fis = new FileInputStream(new File(path));
            //通过构造函数传参
            workbook = new HSSFWorkbook(fis);
            //获取工作表
            HSSFSheet sheetB = workbook.getSheetAt(0);
            Integer rowBNum = sheetB.getLastRowNum() + 1;
            //遍历转对象，不包括首行
            for (int i = 1; i < rowBNum; i++) {
                HSSFRow rowB = sheetB.getRow(i);
                if (rowB == null) {
                    continue;
                }
                Btable tableB = new Btable();
                if (rowB.getCell(0) != null) {
                    tableB.setName(rowB.getCell(0).getStringCellValue());
                }
                if (rowB.getCell(1) != null) {
                    tableB.setWeight(rowB.getCell(1).getNumericCellValue());
                }
                sheetBList.add(tableB);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(workbook != null){
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sheetBList;
    }
}
