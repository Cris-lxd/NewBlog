package com.lxd.web;/*
 *  create by 20224
 *  2020/9/30
 * */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxd.mapper.FileMapper;
import com.lxd.po.User;
import com.lxd.util.Pagination;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.io.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class UploadTest {

    private final static String LOCAL_PATH = "/Users/cris/Documents/Project/gitProjects/NewBlog/uploadFile/";
    private final static String SERVER_PATH = "/usr/uploadFile/";

    @Autowired
    private FileMapper fileMapper;


    @RequestMapping("/tofileUpload")
    public String tofileUpload(Model model) {
//        List<Map> maps = fileMapper.selectAll();
//        for (int i = 0; i < maps.size(); i++) {
//            maps.get(i).put("num", i + 1);
//        }
//        model.addAttribute("file", maps);
        return "fileUploadTestLXD";
    }

    @ResponseBody
    @RequestMapping("/tofileUploadLXD")
    public Object tofileUploadLXD(String index) {
        index = index == null ? "1" : index;
        int fromIndex = (Integer.valueOf(index) - 1) * 10;
        int endIndex = 10;
        List<Map> list = fileMapper.selectAll();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).put("num", i + 1);
        }
        Pagination page = new Pagination(Integer.valueOf(index), 10, list);
        return page;
    }

    @ResponseBody
    @RequestMapping("/fileUpload")
    public String fileUpload(HttpServletRequest request, HttpSession session, @RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        int num = 0;
        try {
            String s = "";
            String filename = file.getOriginalFilename();
            if (filename == null || "".equals(filename)) {
                attributes.addFlashAttribute("message", "????????????");
            }

            String macName = InetAddress.getLocalHost().toString();   //??????????????????
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            String path = "CrisdeMacBook-Pro.local/127.0.0.1".equals(macName) == true ? LOCAL_PATH : SERVER_PATH;
//            String path = "/usr/uploadFile/";
            //String path ="E:/uploadFile/";
            File file1 = new File(path);
            if (!file1.exists()) {
                file1.mkdirs();
            }
            //???????????????????????????????????????
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String date = sdf.format(d);
            String name = date + "_" + filename;
            FileOutputStream fs = new FileOutputStream(path + name);

            InputStream stream = file.getInputStream();
            int size = stream.available();
            byte[] b = new byte[stream.available()];
            int bytesum = 0;
            int byteread = 0;
            while ((byteread = stream.read(b)) != -1) {
                bytesum += byteread;
                fs.write(b, 0, byteread);
                fs.flush();
            }
            ;
            //fs.write(b);
            fs.close();
            stream.close();
            Long userId = fileMapper.getUserId("Cris");
            Map map = new HashMap<>();
            map.put("name", name);
            map.put("realName", filename);
            map.put("path", path);
            map.put("user_id", userId);

            //????????????
            String Mb = "";
            String Kb = "";
            if (size / 1048576 >= 1) {
                //?????????????????????????????????
                float f = (float) size / 1048576;
                DecimalFormat df = new DecimalFormat("0.0");
                String format = df.format(f);
                final String substring = format.substring(format.lastIndexOf(".") + 1, format.lastIndexOf(".") + 2);
                if ("0".equals(substring)) {
                    Kb = format.substring(0, format.lastIndexOf(".")) + "mb";
                } else {
                    Kb = format + "mb";
                }
                map.put("size", Kb);
            } else {
                double ceil = Math.ceil(size / 1024);
                if (ceil == 0) {
                    Kb = "1kb";
                } else {
                    Kb = ceil + "kb";
                }

                map.put("size", Kb);
            }

            num = fileMapper.insertByFileName(map);


        } catch (Exception ex) {

        }
        Object success = null;
        if (num == 1) {
            attributes.addFlashAttribute("message", "?????????????????????????????? /usr/uploadFile????????????");
            //success = JSONObject.toJSON("?????????????????????????????? /usr/uploadFile????????????");
            return "????????????";
        }
        return "????????????";
        //return "redirect:/test/tofileUpload";
    }


    @RequestMapping("/download/{id}")
    public String download(HttpServletResponse response, @PathVariable("id") String id, RedirectAttributes attributes) {
        try {
            Long lid = Long.valueOf(id);
            String filename = fileMapper.selectById(lid);
            String path = "/usr/uploadFile/" + filename;
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            downloadByInputstream(response, fis, filename, file);
        } catch (FileNotFoundException ex) {
            attributes.addFlashAttribute("message", "?????????????????????????????????");
        } catch (IOException ex) {
            attributes.addFlashAttribute("message", "????????????");
        }
        return "redirect:/test/tofileUpload";
    }

    /**
     * ????????????
     *
     * @param response
     * @param stream
     * @param filename
     * @throws IOException
     */
    public void downloadByInputstream(HttpServletResponse response, FileInputStream stream, String filename, File file) throws IOException {
        InputStream is = new BufferedInputStream(stream);
        byte[] bytes = new byte[is.available()];
        /*is.read(bytes);
        is.close();*/

        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Length", "" + file.length());     //??????????????????
        OutputStream os = response.getOutputStream();

        int lengthOfRead = 0;
        int lengthOfNum = 0;
        while ((lengthOfRead = is.read(bytes)) > 0) {
            lengthOfNum += lengthOfRead;
            os.write(bytes, 0, lengthOfRead);
        }
        //os.write(bytes);
        is.close();
        os.flush();
        os.close();
    }

    //??????
    @ResponseBody
    @RequestMapping("/delete/{id}")
    public String delete(HttpServletResponse response, @PathVariable("id") String id, RedirectAttributes attributes) {
        Long lid = Long.valueOf(Integer.parseInt(id));
        String filename = fileMapper.selectById(lid);
        File file = new File("/usr/uploadFile/" + filename);
        String result = null;
        if (file.exists()) {
            if (file.delete()) {
                int num = fileMapper.DeleteById(lid);
                if (num > 0) {
                     result = "???????????????????????????";
                }
            } else {
                result = "????????????";
            }
        } else {
            result = "????????????????????????????????????";
            fileMapper.DeleteById(lid);
        }
//        return "redirect:/test/tofileUpload";
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "searchByText", method = RequestMethod.POST)
    public Object searchByText(String text, String index) {
        index = index == null ? "1" : index;
        List<Map> list = fileMapper.selectByText(text);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).put("num", i + 1);
        }
        Pagination pagination = new Pagination(Integer.valueOf(index), 10, list);
        return pagination;
    }
}
