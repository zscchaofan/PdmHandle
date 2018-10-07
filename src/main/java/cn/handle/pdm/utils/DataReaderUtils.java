package cn.handle.pdm.utils;

import cn.handle.pdm.pojo.Column;
import cn.handle.pdm.pojo.Key;
import cn.handle.pdm.pojo.PrimaryKey;
import cn.handle.pdm.pojo.Table;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.util.*;

public class DataReaderUtils {


    private static String table = "Table";

    private static String column = "Column";

    private static int i = 1;

    private List<Table> listTables = new ArrayList<>();


    public static void main(String[] args) throws Exception{
        new DataReaderUtils().startWork();

       /* Class<?> cc = Table.class;
        for(Field field : cc.getDeclaredFields()){
            System.out.println(field.getGenericType());
        }
*/
    }
    private  void startWork() throws Exception{
        //创建SAXReader对象
        SAXReader reader = new SAXReader();
        //读取文件 转换成Document
        Document document = reader.read(new File("/Users/zhangshichao/Documents/abc.xml"));
        //获取根节点元素对象
        Element root = document.getRootElement();
        //遍历
        listNodes(root);

        if(listTables != null){

            for(Table table : listTables){
                System.out.println(table.getCode());
                System.out.println(table.getColumns());
                System.out.println(table.getComment());
                System.out.println(table.getCreationDate());
                System.out.println(table.getCreator());
                System.out.println(table.getId());
                System.out.println(table.getKeys());
                System.out.println(table.getModificationDate());
                System.out.println(table.getModifier());
                System.out.println(table.getName());
                System.out.println(table.getObjectID());
                System.out.println(table.getPrimaryKey());
                System.out.println(table.getTotalSavingCurrency());
            }
        }

    }
    //遍历当前节点下的所有节点
    private  List<Table> listNodes(Element node) throws Exception {
        Set<String> nodeNameSet = new HashSet<>();
        if (node.elementIterator().hasNext()) {
            //首先获取当前节点的所有属性节点
            List<Attribute> list = node.attributes();
            //遍历属性节点
            Iterator<Element> iterator = node.elementIterator();
            while (iterator.hasNext()) {
                Element e = iterator.next();
                if (e.getQualifiedName().equals("c:Tables")) {
                    packageTableObject(e);
                } else {
                    listNodes(e);
                }
            }
        }
        return null;
    }


    public String getAttibuteByElemet(Element node,String name){
        //首先获取当前节点的所有属性节点
        List<Attribute> list = node.attributes();
        //遍历属性节点

        Set<String> stringSet = new HashSet<>();
        for (Attribute attribute : list) {
            if(attribute.getName().equals(name)){
                return attribute.getValue();
            }
        }
        return null;
    }

    //处理 c:Tables
    private void packageTableObject(Element table) throws Exception {
        Iterator<Element> iterator = table.elementIterator();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            Iterator<Element> tableList = e.elementIterator();
            Table tableObject = (Table) packageDataFromElementToClass(e,Table.class);

            if(tableObject != null && tableList.hasNext()){

                while (tableList.hasNext()) {
                    Element eObeject = tableList.next();
                    if(eObeject.getQualifiedName().equals("c:Columns")){
                        List<Column> listColumn = packageColumnsObject(eObeject);
                        tableObject.setColumns(listColumn);
                    }
                    if(eObeject.getQualifiedName().equals("c:Keys")){
                        List<Key> listKey = packageKeysObject(eObeject);
                        tableObject.setKeys(listKey);
                    }
                    if(eObeject.getQualifiedName().equals("c:PrimaryKey")){
                        List<Key> listPrimaryKey = packagePrimateKey(eObeject);
                        tableObject.setPrimaryKey(listPrimaryKey);
                    }
                }
                listTables.add(tableObject);
            }
        }
    }

    /**
     * @param column
     */
    //处理 c:Columns
    private List<Column> packageColumnsObject(Element column) throws Exception {
        Iterator<Element> iterator = column.elementIterator();
        List<Column> list = new ArrayList<>();
        while (iterator.hasNext()) {
            Element e = iterator.next();

            Column columnObject = (Column) packageDataFromElementToClass(e,Column.class);
            columnObject.setId(getAttibuteByElemet(e,"Id"));
            list.add(columnObject);
        }
        return list;
    }

    //处理 c:Keys
    private List<Key>  packageKeysObject(Element key) throws Exception {
        Iterator<Element> iterator = key.elementIterator();
        List<Key> list = new ArrayList<>();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            Key keyObject = (Key) packageDataFromElementToClass(e,Key.class);
            keyObject.setId(getAttibuteByElemet(e,"Id"));
            list.add(keyObject);
        }
        return list;
    }

    //处理 c:PrimaryKey
    private List<Key> packagePrimateKey(Element primarykey) throws Exception {
        Iterator<Element> iterator = primarykey.elementIterator();
        List<Key> list = new ArrayList<>();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            Key key = new Key();
            key.setRef(getAttibuteByElemet(e,"Ref"));
            list.add(key);
        }
        return list;
    }

    /**
     * 首字母转小写字母
     * @param name
     * @return
     */
    private String theFirstUppercaseTransferToLowercase(String name){

        if(StringUtils.isEmpty(name)){
            return "";
        }
        if(Character.isLowerCase(name.charAt(0))){
            return name;
        }
        return StringUtils.lowerCase(String.valueOf(name.charAt(0))) + name.substring(1);
    }



    private  Object packageDataFromElementToClass(Element element,Class tClass) throws Exception{
        if(element.elementIterator().hasNext()){
            Object object =  tClass.getDeclaredConstructor().newInstance();

            Iterator<Element> list = element.elementIterator();
            Element e = null;
            while(list.hasNext()){
                e = list.next();
                String value = e.getText().trim();
                String lowCaseName = theFirstUppercaseTransferToLowercase(e.getName());
               if(ReflectionUtils.findField(tClass,lowCaseName) != null
                       && ReflectionUtils.findField(tClass,lowCaseName).getGenericType().toString().contains("java.lang.String")){
                   if(ReflectionUtils.findMethod(tClass,"set" + e.getName(),null) != null){
                       ReflectionUtils.findMethod(tClass,"set" + e.getName(),null).invoke(object,value);
                   }
               }
            }

            return object;
        }
        return null;
    }



}
