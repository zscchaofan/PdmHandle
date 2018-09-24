package cn.handle.pdm.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.*;

public  class SaxReaderUtils {

        private static String path = "/Users/zhangshichao/Documents/class/";

        private int numNow = 1;

        private static String packageName = "package cn.handle.pdm.pojo;";

        private static String importList = "import java.util.List;";

        public static void main(String[] args) {
            try {
                new SaxReaderUtils().test();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private  void test() throws Exception{
            //创建SAXReader对象
            SAXReader reader = new SAXReader();
            //读取文件 转换成Document
            Document document = reader.read(new File("/Users/zhangshichao/Documents/abc.xml"));
            //获取根节点元素对象
            Element root = document.getRootElement();
            //遍历
            listNodes(root);

        }
        //遍历当前节点下的所有节点
        private  void listNodes(Element node) {
            System.out.print("当前节点的名称：" + node.getName() + "---------");
            Set<String> nodeNameSet = new HashSet<>();
            if(node.elementIterator().hasNext()){
                //首先获取当前节点的所有属性节点
                List<Attribute> list = node.attributes();
                //遍历属性节点

                Set<String> stringSet = new HashSet<>();
                for (Attribute attribute : list) {
                    stringSet.add(dealClassName(attribute.getName()));
                }

                Map<String ,Integer> map = new HashMap<>();
                //同时迭代当前节点下面的所有子节点
                Iterator<Element> iterator = node.elementIterator();
                //统计属性出现次数
                while(iterator.hasNext()){
                    String name = iterator.next().getName();
                    if(map.get(name) == null){
                        Integer i = 1;
                        map.put(name,i);
                    }else{
                        Integer ii = map.get(name) + 1 ;
                        map.put(name,ii);
                    }
                }

                Set<String> stringSetSingle = new HashSet<>();
                Set<String> stringSetList = new HashSet<>();
                iterator = node.elementIterator();
                while(iterator.hasNext()){
                    Element nodeChildren = iterator.next();
                    String name = nodeChildren.getName();
                    if(map.get(name) == null || map.get(name).intValue() == 1){
                        if(!checkIsHasChildrenNode(nodeChildren)){
                            stringSet.add(dealClassName(name));
                        }else{
                            stringSetSingle.add(dealClassName(name));
                        }
                    }else{
                        stringSetList.add(dealClassName(name));
                    }
                }

                printToFile(dealClassName(node.getName()),editJavaFile(dealClassName(node.getName()),stringSet,stringSetList,stringSetSingle));

                iterator = node.elementIterator();
                while (iterator.hasNext()) {

                    Element e = iterator.next();
                    if(!nodeNameSet.contains(e.getName())){
                        nodeNameSet.add(e.getName());
                        listNodes(e);
                    }
                }
            }else{
                System.out.println("\n");
            }
        }

        //检查是否有子节点
        private boolean checkIsHasChildrenNode(Element node){
            if(node.elementIterator().hasNext()){
                return true;
            }
            return false;
        }

        private String editJavaFile(String className,Set<String> singleFields,Set<String> listFields, Set<String> singleClassField){
            String singlePrefix = "private String ";
            String singlePrivatePrefix = "private ";
            String listPrefix = "private List<";
            String listSuffix = "> ";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(packageName).append("\n");
            if(CollectionUtils.isNotEmpty(listFields)){
                stringBuilder.append(importList).append("\n");
            }
            stringBuilder.append("public class ").append(className).append(" {").append("\n");
            if(CollectionUtils.isNotEmpty(singleFields)){
                for(String singField : singleFields){
                    stringBuilder.append("    ").append(singlePrefix).append(theFirstUppercaseTransferToLowercase(singField)).append(";\n");
                }
            }
            if(CollectionUtils.isNotEmpty(singleClassField)){
                for(String singField : singleClassField){
                    stringBuilder.append("    ").append(singlePrivatePrefix).append(singField).append(" ").append(theFirstUppercaseTransferToLowercase(singField)).append(";\n");
                }
            }
            if(CollectionUtils.isNotEmpty(listFields)){
                for(String listField : listFields){
                    stringBuilder.append("    ").append(listPrefix).append(listField).append(listSuffix).append(" ").append(theFirstUppercaseTransferToLowercase(listField)).append("s").append(";\n");
                }
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        //输出属性信息到java文件中
        private void printToFile(String fileName,String fileString){

            File file = new File(path + fileName+".java");
            if(file.exists()){
                file = new File(path+fileName+String.valueOf(numNow ++ )+".java");
            }
            System.out.println(file.getPath());
            OutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(fileString.getBytes());
                fileOutputStream.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String dealClassName(String name){
            if(name.equals("Key.Columns")){
                System.out.println(name);
            }
            return name.replace(".","");
        }


        //判断当前属性是否输出到类的内容中  xmlns:a 这种属性就不输出
        private boolean isOrNotToPrintThisField(String fieldName){

            if(StringUtils.isEmpty(fieldName)){
                return false;
            }
            String[] fields = fieldName.split(":");
            if(fields.length < 2){
                return true;
            }

            return false;
        }

        //首字母转为小写字母
        private String theFirstUppercaseTransferToLowercase(String name){

            if(StringUtils.isEmpty(name)){
                return "";
            }

            if(Character.isLowerCase(name.charAt(0))){
                return name;
            }
            return StringUtils.lowerCase(String.valueOf(name.charAt(0))) + name.substring(1);
        }


}
