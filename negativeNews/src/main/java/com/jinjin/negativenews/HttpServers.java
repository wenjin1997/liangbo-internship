package com.jinjin.negativenews;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 文进
 * @version 1.0
 */
@RestController
public class HttpServers {
    @RequestMapping("/negativeNews")
    public List<NegativeNews> returnJson( ) {
        // 获取百度新闻中热点新闻的标题和链接
        HashMap<String, String> news = new News().getNews("http://news.baidu.com/");
        // 负面新闻
        HashMap<String, String> negativeNews = new HashMap<>();

        // 通过新闻标题得到的情感值，将负面新闻添加到 negativeNews 中
        for (Map.Entry<String, String> entry : news.entrySet()) {
            // 得到情感API返回的结果，例如 data = {"result":0.739306}
            String data = UrlUtils.sendPost("http://baobianapi.pullword.com:9091/get.php", entry.getKey());
            // 提取结果中的分数值 data = {"result": score}
            String score = data.substring(10, data.length() - 1);
            // 将分数值 score 转换成double数值，方便找到负面情绪的新闻
            double emotion = Double.parseDouble(score);
            // 将负面新闻添加到 negativeNews 中
            if (emotion < -0.5) {
                negativeNews.put(entry.getKey(), entry.getValue());
            }
        }

        // 返回 JSON 数据格式
        List<NegativeNews> negativeNewsList = new ArrayList<>();
        for (Map.Entry<String, String> entry : negativeNews.entrySet()) {
            NegativeNews nNews = new NegativeNews(entry.getKey(), entry.getValue());
            negativeNewsList.add(nNews);
        }
        return negativeNewsList;
    }
}
