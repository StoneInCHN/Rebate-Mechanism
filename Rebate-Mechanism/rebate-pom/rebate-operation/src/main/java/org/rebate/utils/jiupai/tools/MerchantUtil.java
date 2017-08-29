package org.rebate.utils.jiupai.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

public class MerchantUtil {
    public MerchantUtil() {
    	
    }

    public void getChkFile(String url, String filePath) {
        try {
            File e = new File(filePath);
            if(!e.exists()) {
                e.createNewFile();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(e));
            String res = sendAndRecv(url, "", "GBK");
            if(!StringUtils.isBlank(res)) {
                bw.write(res);
            }

            bw.close();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    public String sendAndRecv(String url, String buf, String characterSet) throws IOException {
        String charType;
        if("00".equals(characterSet)) {
            charType = "GBK";
        } else if("01".equals(characterSet)) {
            charType = "GB2312";
        } else if("02".equals(characterSet)) {
            charType = "UTF-8";
        } else {
            charType = null;
        }

        String[] resArr = StringUtils.split(buf, "&");
        HashMap reqMap = new HashMap();

        for(int httpsClient = 0; httpsClient < resArr.length; ++httpsClient) {
            String res = resArr[httpsClient];
            int repMsg = StringUtils.indexOf(res, '=');
            String nm = StringUtils.substring(res, 0, repMsg);
            String val = StringUtils.substring(res, repMsg + 1);
            reqMap.put(nm, val);
        }

        SimpleHttpsClient var11 = new SimpleHttpsClient();
        HttpSendResult var12 = var11.postRequest(url, reqMap, 120000, charType);
        String var13 = var12.getResponseBody();
        return var13;
    }

    public static HashMap<String, String> toHashMap(Object object) {
        HashMap data = new HashMap();
        JSONObject jsonObject = JSONObject.fromObject(object);
        Iterator it = jsonObject.keys();

        while(it.hasNext()) {
            String key = String.valueOf(it.next());
            String value = (String)jsonObject.get(key);
            data.put(key, value);
        }

        return data;
    }
}
