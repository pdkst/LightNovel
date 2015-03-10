package com.novel.lightnovel.Utils;

import android.content.ContentValues;
import android.util.Log;

import com.novel.lightnovel.DataBase.D;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlParser {
    Document document = null;
    private static final String TAG = "HtmlParser";
    public static int VOLLIST = 11;
    public static int BOOK = 12;
    public static int VIEW = 13;

    private HtmlParser() {
    }

//    private HtmlParser(Document document) {
//        this.document = document;
//    }
//
//    private HtmlParser(String path) throws IOException {
//        document = Jsoup.connect(path).data("query", "Java") // 请求参数
//                .userAgent("Android") // 设置// User-Agent 一定要设置UA标识别
//                .cookie("auth", "token") // 设置 cookie
//                .timeout(3000) // 设置连接超时时间
//                .get();
//    }
//
//    private HtmlParser(String path, int TAG) throws IOException {
//        document = Jsoup.connect(path).data("query", "Java") // 请求参数
//                .userAgent("IE11") // 设置// User-Agent 一定要设置UA标识别
//                .cookie("auth", "token") // 设置 cookie
//                .timeout(3000) // 设置连接超时时间
//                .get();
//    }

    public static String getVolPath(int v_id) {
        return "http://lknovel.lightnovel.cn/main/vollist/" + v_id + ".html";
    }

    public static String getBookPath(int b_id) {
        return "http://lknovel.lightnovel.cn/main/book/" + b_id + ".html";
    }

    public static String getViewPath(int view_id) {
        return "http://lknovel.lightnovel.cn/main/view/" + view_id + ".html";
    }

    public static String getSearchPath(String str) {
        try {
            return "http://lknovel.lightnovel.cn/main/booklist/" + URLEncoder.encode(str,"utf-8") + ".html";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG,"-----HtmlParser------>字符串转换失败");
            return "";
        }
    }

    public static int getId(String link) {
        Log.e(TAG,"---------link->id---->"+link);
        if (link!=null)return Integer.parseInt(link.substring(link.lastIndexOf("/") + 1, link.lastIndexOf(".")));
        else return -1;
    }

    public static String getIdString(String link) {
        Log.e(TAG,"---------link->id---->"+link);
        if (link!=null) return link.substring(link.lastIndexOf("/") + 1, link.lastIndexOf("."));
        return "";
    }

    public static String subTime(String str){
        Log.e(TAG,"---------link->id---->"+str);
        return str.substring(0,str.lastIndexOf(" "));
    }


    /**
     * 从path：url网址获取到document对象，返回Android内容
     */
    public static Document mDoc(String path) throws IOException {
        Document document = Jsoup.connect(path).data("query", "Java") // 请求参数
                .userAgent("Android")   // 设置// User-Agent 一定要设置UA标识别
                .cookie("auth", "token") // 设置 cookie
                .timeout(5000)          // 设置连接超时时间
                .get();
        return document;
    }

    /**
     * 从path：url网址获取到document对象，有tag则返回ie10浏览器内容
     */
    public static Document doc(String path) throws IOException {
        Document document = Jsoup.connect(path)//.data("query", "Java") // 请求参数
                .userAgent("IE10") // 设置 User-Agent 一定要设置UA标识别
                        //.cookie("auth", "token") // 设置 cookie
                .timeout(5000) // 设置连接超时时间
                .get();
        return document;
    }

    public static Document indexDoc() throws IOException {
        String url = "http://lknovel.lightnovel.cn/";
        Document document = Jsoup.connect(url).data("query", "Java") // 请求参数
                .userAgent("IE12") // 设置 User-Agent 一定要设置UA标识别
                .cookie("auth", "token") // 设置 cookie
                .timeout(5000) // 设置连接超时时间
                .get();
        return document;
    }

    public static Document parse(String html) {
        return Jsoup.parse(html,"http://lknovel.lightnovel.cn/");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * path：url地址 从path网址获取章节小说的文本内容
     */
    public static StringBuffer getText(String path) {
        StringBuffer strBuffer = new StringBuffer("");
        try {
            Document document = mDoc(path);
            Elements lines = document.select("[class=lk-view-line]");
            for (Element t : lines) {
                strBuffer.append(t.text()).append("\n");
            }
            return strBuffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static StringBuffer getText(Document document) {
        StringBuffer strBuffer = new StringBuffer("");
        Elements lines = document.select("[class=lk-view-line]");
        for (Element t : lines) {
            strBuffer.append(t.text()).append("\n");
        }
        return strBuffer;
    }

    public static StringBuilder getTextB(Document document) {
        StringBuilder stringBuilder = new StringBuilder("");
        Elements lines = document.select("[class=lk-view-line]");
        for (Element t : lines) {
            stringBuilder.append(t.text()).append("\n");
        }
        return stringBuilder;
    }

    //获取插画（只在view的第一个
    public static List<String> getIllustrationList(Document document) {
        List<String> imgList = new ArrayList<String>();
        Elements imgs = document.select("img[data-cover]");
        for (int i = 0; i < imgs.size(); i++) {
            imgList.add("http://lknovel.lightnovel.cn" + imgs.get(i).attr("data-cover"));
        }
        return imgList;
    }

    //获取插画（只在view的第一个
    public static List<ContentValues> getIllustration(int v_id, int b_id, Document document) {
        List<ContentValues> imgList = new ArrayList<>();
        ContentValues cv = null;
        Elements imgs = document.select("img[data-cover]");
        for (int i = 0; i < imgs.size(); i++) {
            cv = new ContentValues();
            String link = "http://lknovel.lightnovel.cn" + imgs.get(i).attr("data-cover");
            String i_id = link.substring(link.lastIndexOf("_") + 1, link.lastIndexOf("."));
            cv.put(D.i_id, i_id);
            cv.put(D.i_link, link);
            cv.put(D.v_id, v_id);
            cv.put(D.b_id, b_id);
            imgList.add(cv);
        }
        return imgList;
    }

    /**
     * 从单卷小说页book获取一卷小说的主要信息
     * UA=ie11
     * book
     */
    public static List<String> getInfo_Book(Document document) {
        List<String> list = new ArrayList<String>();
        Elements tds = document.select("td");
        list.add(tds.get(1).text());    //卷名·标题	0
        list.add(tds.get(3).text());    //作者		1
        list.add(tds.get(5).text());    //插画		2
        list.add(tds.get(7).text());    //文库		3
        list.add(tds.get(9).text());    //浏览数量	4
        list.add(tds.get(11).text());    //更新时间	5
        list.add(tds.get(13).text());    //收藏数量	6
        Elements a = document.select("a[href*=http://lknovel.lightnovel.cn/main/vollist/]");
        String str = a.first().attr("href");
        String vollistId = str.substring(str.lastIndexOf("/") + 1, str.lastIndexOf("."));
        list.add(vollistId);//父类vollistId			7
        return list;
    }

    /**
     * path:url地址 从path网址获取该卷小说的封面图
     * 一般path为单卷小说的详情页面
     * book
     */
    public static String getCover_Book(Document document) {
        String url = null;
        Elements a_Elements = document.select("[class=lk-m-book-cover]");
        Elements cover = a_Elements.select("img");
        url = "http://lknovel.lightnovel.cn" + cover.attr("src");
        return url;
    }

    public static List<String> getCoverList(Document document) {
        Elements imgs = document.select("img:not([style])").select("img:not([class])");
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < imgs.size(); i++) {
            list.add(imgs.get(i).attr("src"));
        }
        Elements cover_main = document.select("img[data-cover]");
        list.add(cover_main.attr("data-cover"));
        return list;
    }

    public static String getCover(Document document) {
        Elements cover_main = document.select("img[data-cover]");
        return cover_main.attr("data-cover");
    }

    /**
     * 获取卷标题和连接
     */
    public static List<List<String>> getList_Book_PC(Document document) {
        Elements as = document.select("a:has(span)");
        Elements spans = as.select("span");
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> alist = null;
        for (int i = 0; i < as.size(); i++) {
            alist = new ArrayList<String>();
            alist.add(spans.get(i).text());     //标题0
            alist.add(as.get(i).attr("href"));  //链接1
            list.add(alist);
        }
        return list;

    }

    /**
     * 获取单卷小说章节名(title)和连接(link)的集合的hashMap，
     * 获取章节目录和内容的主力
     * book
     */
    public static HashMap<String, List<String>> getMapFromBook(Document document) {
        List<String> linkList = null;
        List<String> titleList = null;
        Elements titles = document.select("[class=lk-m-book-title]")
                .select("a");
        linkList = new ArrayList<String>();
        titleList = new ArrayList<String>();
        for (Element e : titles) {
            linkList.add(e.attr("href"));
            titleList.add(e.text());
        }
        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("title", titleList);
        map.put("link", linkList);
        return map;
    }

    /**
     * 从搜索到的内容页获取小说列表
     */
    public static List<ContentValues> getListFromSearch(Document document) {
        ContentValues a = new ContentValues();
        List<ContentValues> list = new ArrayList<>();
        Elements as = document.select(".lk-ellipsis");
        if (as.size() == 0) return list;
        else if (as.size() == 1) {
            a.put(D.v_id, getId(as.attr("href")));
            a.put(D.v_name, as.text());
            list.add(a);
            return list;
        } else {
            a.put(D.v_name, as.get(0).text());
            a.put(D.v_id, getId(as.get(0).attr("href")));
            Map<String, String> map = new HashMap<String, String>();
            map.put(as.get(0).attr("href"), "href");
            list.add(a);
            for (int i = 0; i < as.size(); i++) {
                Element asi = as.get(i);
                if (map.get(asi.attr("href")) == null) {
                    a = new ContentValues();
                    a.put(D.v_name, asi.text());        //0标题
                    a.put(D.v_id, getId(asi.attr("href")));  //1链接
                    map.put(asi.attr("href"), "href");
                    list.add(a);
                }
            }
        }
        return list;
    }

    /**
     * 从所有卷小说path产生的document对象中解析所有卷小说列表
     * vollist---->allBookList
     */
    public static List<List<ContentValues>> getViewsList(int v_id, Document document) {
        ContentValues cv = null;
        Elements uls = document.select("ul");
        List<List<ContentValues>> booklist = new ArrayList<>();//卷列表
        for (int i = 1; i < uls.size(); i++) {
            List<ContentValues> viewList = new ArrayList<>();//章节列表

            Elements lis = uls.get(i).select("li");
            for (int j = 0; j < lis.size(); j++) {
                cv = new ContentValues();
                Elements as = lis.get(1).select("a");

                cv.put(D.view_id, getId(as.attr("href")));   //view_id
                cv.put(D.view_name, as.text());             //view_name
                cv.put(D.v_id, v_id);
//                cv.put("b_id",b_id);
                viewList.add(cv);
            }
            booklist.add(viewList);
        }
        return booklist;
    }

    /**
     * 从小说主页网页的document对象中取出小说（vollist）信息，返回map对象
     * vollist
     *
     * @param v_id     vollist id
     * @param document 获取到的document对象
     */
    public static ContentValues getVollist(int v_id, Document document) {
        ContentValues cv = new ContentValues();

        Elements s = document.select("strong");
        cv.put(D.v_id, v_id);
        cv.put(D.v_name, s.first().text());                            //小说名
        cv.put(D.v_cover, getCover(document));                       //封面

        Elements tds = document.select("td");
        cv.put(D.author, tds.get(1).text());                        //0作者
        cv.put(D.illustration, tds.get(3).text());                    //1插画
        cv.put(D.library, tds.get(5).text());                        //2文库
        cv.put(D.counts, tds.get(7).text());                         //3浏览数量
        cv.put(D.updatetime, tds.get(9).text());                    //4更新时间
        cv.put(D.last_title, tds.select("a").get(1).text());        //5最新章节
        Log.e(TAG, "------vollist--------->" + tds.get(9).text());

        cv.put(D.last_id, getIdString(tds.select("a").get(1).attr("href")));    //6更新章节（book
        Elements ps = document.select("p.ft-12");
        cv.put(D.about, ps.text());                                 //7简介
        return cv;
    }

    /**
     * 从小说主页的document对象中获取单卷链接book和名称,封面
     * vollist获取 book
     *
     * @param v_id     vollist id
     * @param document 获取到的document对象
     * @return 包含contentValue的集合
     */
    public static List<ContentValues> getBooks(int v_id, Document document) {
        List<ContentValues> list = new ArrayList<ContentValues>();
        ContentValues cv = null;
        Elements as = document.select("strong:has(a)").select("a");
        List<String> covers = getCoverList(document);
        for (int i = 0; i < as.size(); i++) {
            cv = new ContentValues();
            Log.e(TAG, "------get-books--------->" + as.get(i).attr("href"));
            cv.put(D.b_id, getIdString(as.get(i).attr("href")));                     //单卷链接
            cv.put(D.b_name, as.get(i).text().replace(Jsoup.parse("&nbsp").text(), ""));//卷标题
            cv.put(D.b_cover, covers.get(i));                                            //封面链接
            cv.put(D.v_id, v_id);                                                    //外键v_id
            list.add(cv);
        }
        return list;
    }

    public static Map<String, List<ContentValues>> getAll(int v_id, Document document) {
        Map<String, List<ContentValues>> all = new HashMap<>();
        ContentValues vol = new ContentValues();

        Elements s = document.select("strong");
        vol.put(D.v_id, v_id);
        vol.put(D.v_name, s.first().text());                            //小说名
        vol.put(D.v_cover, getCover(document));                       //封面

        Elements tds = document.select("td");
        vol.put(D.author, tds.get(1).text());                        //0作者
        vol.put(D.illustration, tds.get(3).text());                    //1插画
        vol.put(D.library, tds.get(5).text());                        //2文库
        vol.put(D.counts, tds.get(7).text());                         //3浏览数量
        vol.put(D.updatetime, tds.get(9).text());                    //4更新时间
        vol.put(D.last_title, tds.select("a").get(1).text());        //5最新章节
//        Log.e(TAG, "------get-all--------->" + tds.select("a").get(1).attr("href"));
        vol.put(D.last_id, getIdString(tds.select("a").get(1).attr("href")));    //6更新章节（book

        Elements ps = document.select("p.ft-12");
        vol.put(D.about, ps.text());                                 //7简介

        List<ContentValues> vols = new ArrayList<>();
        vols.add(vol);
        all.put("vol", vols);

        //------------book-------------------------
        List<ContentValues> books = new ArrayList<ContentValues>();//book信息
        List<ContentValues> views = new ArrayList<ContentValues>();//章节信息（由于先储存到数据库，所以没有分级储存

        Elements as = document.select("strong:has(a)").select("a");
        List<String> covers = getCoverList(document);

        Elements uls = document.select("ul");   //view单元格

        ContentValues book = null;//临时存储的book单元
        ContentValues view = null;//临时存储的view单元
        int b_id;
        for (int i = 0; i < as.size(); i++) {
            book = new ContentValues();
            b_id = getId(as.get(i).attr("href"));
            book.put(D.b_id, b_id);                                                     //b_id
            book.put(D.b_name, as.get(i).text().replace(Jsoup.parse("&nbsp").text(), ""));  //b_name
            book.put(D.b_cover, covers.get(i));                                         //b_cover
            book.put(D.v_id, v_id);                                                     //外键v_id
            books.add(book);

            Elements lis = uls.get(i+1).select("li");
            for (int j = 0; j < lis.size(); j++) {
                view = new ContentValues();
                Elements view_as = lis.get(j).select("a");

//                Log.e(TAG, "------get-all-->views------->" +view_as.attr("href")+"<-----?");
                view.put(D.view_name, view_as.text());              //view_name
                view.put(D.view_id, getId(view_as.attr("href")));   //view_id
                view.put(D.v_id, v_id);                             //外键v_id
                view.put(D.b_id, b_id);                              //外键b_id
                views.add(view);
            }
        }
        all.put("book", books);
        all.put("view", views);
        return all;
    }


    /**
     * 返回path路径下载的InputStream;
     * @param path 文件路径
     */
    public static InputStream getInputStream(String path) throws IOException {
        InputStream inputStream = null;

        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(3000);
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        int code = connection.getResponseCode();
        if (code == 200) {
            inputStream = connection.getInputStream();
        }
        return inputStream;

    }

    public static List<List<String>> getBooklist_index_android() {
        String url = "http://lknovel.lightnovel.cn/mobile/index.html";
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> books = null;
        try {
            Document document = HtmlParser.mDoc(url);
            Elements as = document.select("a[class=J_datalz]");
            Elements names = document.select("p:not([class])");
            Elements titles = document.select("p[class]");
            for (int i = 0; i < as.size(); i++) {
                books = new ArrayList<String>();
                books.add(String.valueOf(i));            //循环参数i
                books.add(names.get(i).text());            //书名
                books.add(titles.get(i).text());        //卷名
                books.add(as.get(i).attr("href"));        //链接
                books.add(as.get(i).attr("data-cover"));//封面
                list.add(books);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<List<List<String>>> getBooklist_index_pc() {
        String url = "http://lknovel.lightnovel.cn/";
        List<List<List<String>>> list = new ArrayList<List<List<String>>>();
        List<List<String>> area = new ArrayList<List<String>>();
        List<String> vols = null;
        try {
            Document document = doc(url);
            Elements uls = document.select("ul");
            Elements a1 = uls.get(1).select("a:has(img)");    //最新新番
            for (int i = 0; i < a1.size(); i++) {
                vols = new ArrayList<String>();
                vols.add(a1.get(i).select("img").attr("alt"));    //vol书名
                vols.add(a1.get(i).attr("href"));                //vol链接
                vols.add(a1.get(i).select("img").attr("data-cover"));//封面链接
                area.add(vols);
            }
            list.add(area);
            area = new ArrayList<List<String>>();
            Elements a2 = uls.get(2).select("a:has(img)");    //动画化
            for (int i = 0; i < a2.size(); i++) {
                vols = new ArrayList<String>();
                vols.add(a2.get(i).select("img").attr("alt"));    //vol书名
                vols.add(a2.get(i).attr("href"));                //vol链接
                vols.add(a2.get(i).select("img").attr("data-cover"));//封面链接
                area.add(vols);
            }
            list.add(area);
            area = new ArrayList<List<String>>();
            Elements a3 = uls.get(3).select("a:has(img)");    //热门
            for (int i = 0; i < a3.size(); i++) {
                vols = new ArrayList<String>();
                vols.add(a3.get(i).select("img").attr("alt"));    //vol书名
                vols.add(a3.get(i).attr("href"));                //vol链接
                vols.add(a3.get(i).select("img").attr("data-cover"));//封面链接
                area.add(vols);
            }
            list.add(area);
            area = new ArrayList<List<String>>();
            Elements a4 = uls.get(4).select("a:has(img)");  //最新
            for (int i = 0; i < a4.size(); i++) {
                vols = new ArrayList<String>();
                vols.add(a4.get(i).select("img").attr("alt"));    //vol书名
                vols.add(a4.get(i).attr("href"));                //vol链接
                vols.add(a4.get(i).select("img").attr("data-cover"));//封面链接
                area.add(vols);
            }
            list.add(area);
            area = new ArrayList<List<String>>();
            Elements a5 = uls.get(5).select("span");        //排行榜
            for (int i = 0; i < a5.select("a").size(); i++) {
                vols = new ArrayList<String>();
                vols.add(a5.select("a").get(i).text());            //书名
                vols.add(a5.select("a").get(i).attr("href"));    //链接
                vols.add(a5.select("[class=inline lk-count]").get(i).text());//点击量
                area.add(vols);
            }
            list.add(area);
            area = new ArrayList<List<String>>();
            Elements a6 = uls.get(6).select("li").select("a");//随机推荐
            for (int i = 0; i < a6.size(); i++) {
                vols = new ArrayList<String>();
                vols.add(a6.get(i).text());                        //书名
                vols.add(a6.get(i).attr("href"));                //链接
                area.add(vols);
            }
            list.add(area);
            area = new ArrayList<List<String>>();
            Elements a7 = uls.get(7).select("a");               //收藏排行
            for (int i = 0; i < a7.select("a[class=ft-14]").size(); i++) {
                vols = new ArrayList<String>();
                vols.add(String.valueOf(i));        //循环参数i
                vols.add(a7.select("a[class=ft-14]").get(i).text());    //标题
                vols.add(a7.select("a[class=ft-14]").get(i).attr("href"));//链接
                vols.add(a7.select("img").get(i).attr("data-cover"));   //封面
                area.add(vols);
            }
            list.add(area);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<List<ContentValues>> getvollist_index_pc(Document document) {
        List<List<ContentValues>> list = new ArrayList<>();
        List<ContentValues> area = null;

        Elements uls = document.select("ul");

        area = getIndex_last_anime(document);
        list.add(area);
        area = getIndex_furture_anime(document);
        list.add(area);
        area = getIndex_hot(document);
        list.add(area);
        area = getIndex_last_vol(document);
        list.add(area);
        area = getIndex_list(document);
        list.add(area);
        area = getIndex_radom(document);
        list.add(area);
        area = getIndex_favoList(document);
        list.add(area);
        return list;
    }

    public static List<ContentValues> getIndex_favoList(Document document) {
        Elements uls = document.select("ul");
        List<ContentValues> area;
        ContentValues vol;
        area = new ArrayList<>();
        Elements a7 = uls.get(7).select("a");               //收藏排行6
        for (int i = 0; i < a7.select("a[class=ft-14]").size(); i++) {
            vol = new ContentValues();
            vol.put(D.v_name, a7.select("a[class=ft-14]").get(i).text());    //标题
            vol.put(D.v_id, getId(a7.select("a[class=ft-14]").get(i).attr("href")));//链接
            vol.put(D.v_cover, a7.select("img").get(i).attr("data-cover"));   //封面
            area.add(vol);
        }
        return area;
    }

    public static List<ContentValues> getIndex_radom(Document document) {
        Elements uls = document.select("ul");
        List<ContentValues> area;
        ContentValues vol;
        area = new ArrayList<>();
        Elements a6 = uls.get(6).select("li").select("a");//随机推荐5
        for (int i = 1; i < a6.size(); i++) {
            vol = new ContentValues();
            vol.put(D.v_name, a6.get(i).text());                        //书名
            vol.put(D.v_id, getId(a6.get(i).attr("href")));                //链接
            area.add(vol);
        }
        return area;
    }

    public static List<ContentValues> getIndex_list(Document document) {
        Elements uls = document.select("ul");
        List<ContentValues> area;
        ContentValues vol;
        area = new ArrayList<>();
        Elements a5 = uls.get(5).select("span");        //排行榜4
        for (int i = 0; i < a5.select("a").size(); i++) {
            vol = new ContentValues();
            Element ai = a5.get(i);
            vol.put(D.v_name, a5.select("a").get(i).text());            //书名
            vol.put(D.v_id, getId(a5.select("a").get(i).attr("href")));    //链接
            vol.put(D.counts, a5.select("[class=inline lk-count]").get(i).text());//点击量
            area.add(vol);
        }
        return area;
    }

    public static List<ContentValues> getIndex_last_vol(Document document) {
        Elements uls = document.select("ul");
        List<ContentValues> area;
        ContentValues vol;
        area = new ArrayList<>();
        Elements a4 = uls.get(4).select("a:has(img)");  //最新3
        for (int i = 0; i < a4.size(); i++) {
            vol = new ContentValues();
            Element ai = a4.get(i);
            vol.put(D.v_name, ai.select("img").attr("alt"));    //vol书名
            vol.put(D.v_id, getId(ai.attr("href")));                //vol链接
            vol.put(D.v_cover, ai.select("img").attr("data-cover"));//封面链接
            area.add(vol);
        }
        return area;
    }

    public static List<ContentValues> getIndex_hot(Document document) {
        Elements uls = document.select("ul");
        List<ContentValues> area;
        ContentValues vol;
        area = new ArrayList<>();
        Elements a3 = uls.get(3).select("a:has(img)");    //热门2
        for (int i = 0; i < a3.size(); i++) {
            vol = new ContentValues();
            Element ai = a3.get(i);
            vol.put(D.v_name, ai.select("img").attr("alt"));    //vol书名
            vol.put(D.v_id, getId(ai.attr("href")));                //vol链接
            vol.put(D.v_cover, ai.select("img").attr("data-cover"));//封面链接
            area.add(vol);
        }
        return area;
    }

    public static List<ContentValues> getIndex_furture_anime(Document document) {
        Elements uls = document.select("ul");
        List<ContentValues> area;
        ContentValues vol;
        area = new ArrayList<>();
        Elements a2 = uls.get(2).select("a:has(img)");    //动画化1
        for (int i = 0; i < a2.size(); i++) {
            vol = new ContentValues();
            Element ai = a2.get(i);
            vol.put(D.v_name, ai.select("img").attr("alt"));    //vol书名
            vol.put(D.v_id, getId(ai.attr("href")));                //vol链接
            vol.put(D.v_cover, ai.select("img").attr("data-cover"));//封面链接
            area.add(vol);
        }
        return area;
    }

    public static List<ContentValues> getIndex_last_anime(Document document) {
        Elements uls = document.select("ul");
        List<ContentValues> area;
        ContentValues vol;
        Elements a1 = uls.get(1).select("a:has(img)");    //最新新番0
        area = new ArrayList<>();
        for (int i = 0; i < a1.size(); i++) {
            Element ai = a1.get(i);
            vol = new ContentValues();
            vol.put(D.v_name, ai.select("img").attr("alt"));    //vol书名
            vol.put(D.v_id, getId(ai.attr("href")));                //vol链接
            vol.put(D.v_cover, ai.select("img").attr("data-cover"));//封面链接
            area.add(vol);
        }
        return area;
    }

    public static boolean volTest(Document document) {
        int l = document.toString().indexOf("lknovel.lightnovel.cn");
        return l != -1;
    }

    public static void test() {

    }

}
