package com.novel.lightnovel.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Crash on 2015/1/6.
 */
public class FileFactory {

    public String imaho = null;
    public static FileFactory fileFactory = null;
    private static String type;
    private String TAG = "FileFactory";

    private FileFactory() {
        init();
    }

    /**
     * 构造函数&初始化
     *
     * @param path：SD卡根目录
     */
    private FileFactory(String path) {
        imaho = path + "/imaho";
        init();
    }

    //初始化
    public void init() {
        if (imaho == null || imaho.equals("/imaho"))
            imaho = Environment.getExternalStorageDirectory().toString() + "/imaho";
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            File imahoDir = new File(imaho);
//            File downDir = new File(getVollistDir());
//            File cacheDir = new File(getCacheDir());
//            File coverDir = new File(getCoverDir());
//            if (!imahoDir.exists()) {
//                imahoDir.mkdirs();
//                downDir.mkdirs();
//                cacheDir.mkdirs();
//                coverDir.mkdirs();
//            }
//        }
    }

    /**
     * file工厂类的实例化：
     *
     * @param path 内存卡跟路径
     * @return 返回一个fileFactory对象
     */
    public static FileFactory newInstence(String path) {
        if (fileFactory == null) {
            fileFactory = new FileFactory(path);
            return fileFactory;
        } else return fileFactory;
    }

    /**
     * file工厂类的实例化：
     *
     * @param context 上下文
     * @return 返回一个fileFactory对象
     */
    public static FileFactory newInstence(Context context) {
        if (fileFactory == null) {
            type = PreferenceManager.getDefaultSharedPreferences(context).getString("setting_sd_path", "EXTERNAL_STORAGE");
            if ("EXTERNAL_STORAGE".equals(type) || "SECONDARY_STORAGE".equals(type)) {

                fileFactory = new FileFactory(System.getenv().get(type));
                return fileFactory;
            } else {
                return fileFactory = new FileFactory();
            }
        } else return fileFactory;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 保存byte流到文件，一般保存图片用
     *
     * @param inputStream 输入流
     * @param file        要保存的文件
     * @return 保存成功返回{@code true}。
     */
    private boolean saveBytes(InputStream inputStream, File file) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            int len;
            byte[] data = new byte[1024];
            while ((len = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 保存document文档到文件
     *
     * @param document 要保存的document对象
     * @param file     保存的目标文件
     * @return 保存成功返回{@code true}。
     */
    private boolean save(Document document, File file) {
        Log.d(TAG,"FileFactory------------>file = "+file.getAbsolutePath());
        Log.d(TAG,"FileFactory------------>doc = \n"+file.getAbsolutePath());
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(document.toString().getBytes("UTF-8"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 保存vollist信息到内存卡  //保存&读取 书/卷 信息
     *
     * @param v_id     卷v_id
     * @param document vol_document
     * @return 成功则返回true
     */
    public boolean saveVol(int v_id, Document document) {
        //保存volllist信息
        File file = new File(getVollistDir(v_id), getVolName());
        return getSDstatus() && save(document, file);
    }

    /**
     * 读取vollist信息
     *
     * @param v_id 卷v_id
     * @return 读取失败返回null，成功则返回document对象
     */
    public Document readVol(int v_id) {
        //从内存卡读取vollist信息 id:vollist.getId
        File file = new File(getVollistDir(v_id), getVolName());
        if (getSDstatus() && file.exists()) {
            try {
                return Jsoup.parse(file, "UTF-8", "http://lknovel.lightnovel.cn/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 保存封面
     *
     * @param inputStream 输入流
     * @param v_id        卷id
     * @param b_id        book id
     * @return 保存成功返回true
     */
    public boolean saveCover(InputStream inputStream, int v_id, int b_id) {
        File file = new File(getCoverDir(v_id), getImgName(b_id));
        return getSDstatus() && saveBytes(inputStream, file);
    }

    /**
     * 保存封面到内存卡中
     * @param inputStream 封面的输入流
     * @param v_id vollist id(卷封面，所以文件名置为0；
     * @return 保存成功返回{@code true}；
     */
    public boolean saveCover(InputStream inputStream, int v_id) {
        return saveCover(inputStream, v_id, 0);
    }

    /**
     * 从内存卡中读取封面Drawable
     * @param v_id vollist id
     * @param b_id book id
     * @return  Drawable对象
     */
    public Drawable readCover(int v_id, int b_id) {
        return Drawable.createFromPath(getCoverPath(v_id, b_id));
    }

    /**
     * 保存插画图
     *
     * @param inputStream 输入流
     * @param v_id        卷id
     * @param b_id        book id
     * @param name        插画文件名
     * @return 成功返回true
     */
    public boolean saveIllustration(InputStream inputStream, int v_id, int b_id, String name) {
        File file = new File(getIllustrationDir(v_id, b_id), getImgName(name));
        return getSDstatus() && saveBytes(inputStream, file);
    }

    /**
     * 从内存卡中读取插画到
     * @param v_id    卷id
     * @param b_id    book id
     * @param name    插画文件名
     * @return Drawable对象
     */
    public Drawable readIllustration(int v_id, int b_id, String name) {
        return Drawable.createFromPath(getIllustrationPath(v_id, b_id, name));
    }


    /**
     * @param v_id:卷id
     * @param view_id：章节id
     * @param document：文本内容
     */
    public boolean saveView(int v_id, int view_id, Document document) {
        File bookView = new File(getVollistDir(v_id), getViewName(view_id));
        return getSDstatus() && save(document, bookView);
    }

    /**
     * 从内存卡中读取view的document对象
     * @param v_id 卷id
     * @param view_id 章节id
     * @return view的document对象
     */
    public Document readView(int v_id, int view_id) {
        File file = new File(getVollistDir(v_id), getViewName(view_id));
        if (getSDstatus() && isViewExists(v_id, view_id)) {
            try {
                return Jsoup.parse(file, "UTF-8", "http://lknovel.lightnovel.cn/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 保存推荐页的document对象到sd卡
     * @param document 推荐页的document对象
     * @return 保存成功返回{@code true} ；
     */
    public boolean saveIndex(Document document) {
        File file = new File(getIndexDir(), getIndexName());
        if (getSDstatus()) {
            if (isIndexFilesExists()) {
                File indexDir = new File(getIndexDir());
                File[] files = indexDir.listFiles();
                for (File f : files) f.delete();
            }
            return save(document, file);
        }
        return false;
    }

    /**
     * 从内存卡中读取推荐页面（index）
     *
     * @return index的document对象
     */
    public Document readIndex() {
        File file = new File(getIndexDir(), getIndexName());
        if (getSDstatus() && isIndexExists()) {
            try {
                return Jsoup.parse(file, "UTF-8", "http://lknovel.lightnovel.cn/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 创建vollist文件夹，一般路径为：/sdcard/imaho/down/v_id/
     *
     * @param v_id vollist id
     * @return 成功返回true
     */
    public boolean makeVollistDir(int v_id) {
        if (getSDstatus()) {
            File vollistDir = new File(getVollistDir(v_id));
            return vollistDir.mkdirs();
        }
        return false;
    }

    /**
     * 创建封面文件夹，一般路径为：/sdcard/imaho/cover/v_id/
     *
     * @param v_id vollist id
     * @return 成功返回true
     */
    public boolean makeCoverDir(int v_id) {
        if (getSDstatus()) {
            File coverDir = new File(getCoverDir(v_id));
            return coverDir.mkdirs();
        }
        return false;
    }

    /**
     * 创建插画文件夹一般路径为：/sdcard/imaho/down/v_id/illustration/b_id/
     *
     * @param v_id vollist id
     * @param b_id book id
     * @return 成功返回true
     */
    public boolean makeIllustrationDir(int v_id, int b_id) {
        if (getSDstatus()) {
            File file = new File(getIllustrationDir(v_id, b_id));
            return file.mkdirs();
        }
        return false;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 判断文件或者文件夹是否存在
     *
     * @param path 文件或者文件夹路径
     * @return 存在返回true
     */
    public boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 封面是否存在
     *
     * @param v_id vollist id
     * @param b_id book id
     * @return 存在返回true
     */
    public boolean isCoverExists(int v_id, int b_id) {
        if (getSDstatus()) {
            File cover = new File(getCoverDir(v_id), getImgName(b_id));
            return cover.exists();
        }
        return false;
    }

    public boolean isIllExists(int v_id, int b_id, String name) {
        if (getSDstatus()) {
            File ill = new File(getIllustrationDir(v_id, b_id), getImgName(name));
            return ill.exists();
        }
        return false;
    }

    public boolean isVolExists(int v_id) {
        if (getSDstatus()) {
            File file = new File(getVollistDir(v_id), getVolName());
            return file.exists();
        }
        return false;
    }

    public boolean isViewExists(int v_id, int view_id) {
        if (getSDstatus()) {
            File file = new File(getVollistDir(v_id), getViewName(view_id));
            return file.exists();
        }
        return false;
    }

    /**
     * 检查首页信息的文件夹是否存在首页文件
     *
     * @return 返回{@code true} 如果文件存在
     */
    public boolean isIndexFilesExists() {
        if (getSDstatus()) {
            File file = new File(getIndexDir());
            if (file.exists()) {
                String[] files = file.list();
                return files.length != 0;
            }
        }
        return false;
    }

    /**
     * 检查首页信息的文件是否存在
     *
     * @return 返回{@code true} 如果首页文件存在
     */
    public boolean isIndexExists() {
        if (getSDstatus()) {
            File file = new File(getIndexDir(), getIndexName());
            return file.exists();
        } else return false;
    }

    public boolean isUserImExists() {
        if (getSDstatus()) {
            File file = new File(getImaho(), getUserFavorImName());
            return file.exists();
        }
        return false;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取文件信息
     *
     * @return 返回 {@code true} 如果内置内存卡可用
     */
    public boolean getSDstatus() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public String getVollistPath(int v_id) {
        return getVollistDir(v_id) + "/" + getVolName();
    }

    public String getCoverPath(int v_id) {
        return getCoverDir(v_id) + "/" + getVolCoverName();
    }

    public String getCoverPath(int v_id, int id) {
        return getCoverDir(v_id) + "/" + getImgName(id);
    }

    public String getIllustrationPath(int v_id, int b_id, String name) {
        return getIllustrationDir(v_id, b_id) + "/" + getImgName(name);
    }

    public File getViewfile(int v_id, int view_id) {

        return new File(getVollistDir(v_id), getViewName(view_id));
    }

    public String getViewName(int view_id) {
        return view_id + ".imh";
    }

    public String getImgName(int id) {
        return id + ".img";
    }

    public String getImgName(String id) {
        return id + ".img";
    }

    public String getVolCoverName() {
        return "0.img";
    }

    public String getUserFavorImName() {
        return "user.im";
    }

    public String getVolName() {
        return "vollist.im";
    }

    public String getIndexName() {
        return System.currentTimeMillis() / 86400000 + ".imh";
    }

    public String getIllustrationDir(int v_id, int b_id) {
        return imaho + "/down/" + v_id + "/illustration/" + b_id;
    }

    public String getBookDir(int v_id, int b_id) {
        return imaho + "/down/" + v_id + "/" + b_id + "/";
    }

    public String getVollistDir(int v_id) {
        return imaho + "/down/" + v_id;
    }

    public String getDownDir() {
        return imaho + "/down";
    }

    public String getCoverDir() {
        return imaho + "/cover";
    }

    public String getCoverDir(int v_id) {
        return imaho + "/cover/" + v_id;
    }

    public String getCacheDir() {
        return imaho + "/cache";
    }

    public String getCacheDir(int v_id) {
        return imaho + "/cache/" + v_id;
    }

    public String getIndexDir() {
        return imaho + "/cache/index/";
    }

    public String getImaho() {
        return imaho;
    }

    public void setImaho(String imaho) {
        this.imaho = imaho;
    }
}
