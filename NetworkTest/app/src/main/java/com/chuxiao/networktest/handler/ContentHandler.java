package com.chuxiao.networktest.handler;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by 12525 on 2018/5/4.
 */

public class ContentHandler extends DefaultHandler {
    private String nodeName;

    private StringBuilder id;

    private StringBuilder name;

    private StringBuilder version;

    /**
     * 开始解析XML的时候调用
     */
    @Override
    public void startDocument() throws SAXException {
//        super.startDocument();
        id = new StringBuilder();
        name = new StringBuilder();
        version = new StringBuilder();
    }

    /**
     * 开始解析某个节点的时候调用
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//        super.startElement(uri, localName, qName, attributes);
        //记录当前节点名
        nodeName = localName;
    }

    /**
     * 获取节点内容的时候调用
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
//        super.characters(ch, start, length);
        //根据当前节点名判断将内容添加到哪一个StringBuilder对象中
        if ("id".equals(nodeName)) {
            id.append(ch, start, length);
        } else if ("name".equals(nodeName)) {
            name.append(ch, start, length);
        } else if ("version".equals(nodeName)) {
            version.append(ch, start, length);
        }
    }

    /**
     * 完成解析某个节点的时候调用
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
//        super.endElement(uri, localName, qName);
        if ("app".equals(localName)) {
            Log.d("ContentHandler", "id is " + id.toString().trim());
            Log.d("ContentHandler", "name is " + name.toString().trim());
            Log.d("ContentHandler", "version is " + version.toString().trim());
            // 清空StringBuilder，否则会影响下次读取
            id.setLength(0);
            name.setLength(0);
            version.setLength(0);
        }
    }

    /**
     * 完成解析XML的时候调用
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
