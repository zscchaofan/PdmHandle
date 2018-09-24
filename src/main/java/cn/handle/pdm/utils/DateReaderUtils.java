package cn.handle.pdm.utils;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

public class DateReaderUtils {
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

            Iterator<Element> iterator = node.elementIterator();
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
}
