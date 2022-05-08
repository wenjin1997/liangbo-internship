package com.jinjin.negativenews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author 文进
 * @version 1.0
 */
public class News {
    /**
     *
     * @param url 访问路径
     * @return
     */
    public Document getDocument (String url){
        try {
            //5000是设置连接超时时间，单位ms
            return Jsoup.connect(url).timeout(5000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param url 访问网站的url
     * @return 返回热点新闻的标题和链接
     */
    public HashMap getNews (String url) {
        Document doc = getDocument(url);
        HashMap<String, String> news = new HashMap<>();
        // 获取热点新闻HTML代码
        Elements hotNews = doc.select("[class=mod-tab-pane active]").select("a");
        // 获取新闻标题及链接
        for (int i = 0; i < hotNews.size(); i++) {
            news.put(hotNews.get(i).text(), hotNews.get(i).attr("href"));
//            System.out.println(hotNews.get(i).text() + "   " + hotNews.get(i).attr("href"));
        }
        return news;
    }
}
