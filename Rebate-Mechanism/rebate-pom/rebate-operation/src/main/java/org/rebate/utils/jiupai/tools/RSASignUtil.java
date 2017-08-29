package org.rebate.utils.jiupai.tools;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.rebate.utils.LogUtil;

public class RSASignUtil {
	
    private String service = null;
    private String certFilePath = null;
    private String password = null;
    private String hexCert = null;
    private String rootCertPath = null;
    
    public RSASignUtil() {
    	
    }
    
    public RSASignUtil(String rootCertPath) {
        this.rootCertPath = rootCertPath;
    }
    
    public RSASignUtil(String certFilePath, String password) {
        this.certFilePath = certFilePath;
        this.password = password;
    }

    /**
     * 签名
     */
    public String sign(String indata, String encoding) throws Exception {
        String serverSign = null;
        if(StringUtils.isBlank(encoding)) {
            encoding = "GBK";
        }
        LogUtil.debug(this.getClass(), "sign", "indata:[%s]",indata);
        Map<String, String> signMap = coverString2Map(indata);
        RpmVerifyService rvs = new RpmVerifyService();


        try {
            CAP12CertTool singData = new CAP12CertTool(this.certFilePath, this.password);
            X509Certificate cert = singData.getCert();
            byte[] si = singData.getSignData(indata.getBytes(encoding));
            byte[] cr = cert.getEncoded();
            this.hexCert = HexStringByte.byteToHex(cr);
            serverSign = HexStringByte.byteToHex(si);
        } catch (CertificateEncodingException var8) {
            LogUtil.debug(this.getClass(), "sign", "Certificate encoding exception:%s",var8);
        } catch (FileNotFoundException var9) {
            LogUtil.debug(this.getClass(), "sign", "File not found exception:%s",var9);
        } catch (SecurityException var10) {
        	LogUtil.debug(this.getClass(), "sign", "Security exception:%s", var10);
        }

        return serverSign;
    }

    /**
     * 验签
     */
    public boolean verify(String oridata, String signData, String svrCert, String encoding) throws UnsupportedEncodingException {
        boolean res = false;
        if(StringUtils.isBlank(encoding)) {
            encoding = "GBK";
        }

        try {
            byte[] e = HexStringByte.hexToByte(signData.getBytes());
            byte[] inDataBytes = oridata.getBytes(encoding);
            byte[] signaturepem = checkPEM(e);
            if(signaturepem != null) {
                e = Base64.decode(signaturepem);
            }

            X509Certificate cert = this.getCertFromHexString(svrCert);
            if(cert != null) {
                PublicKey pubKey = cert.getPublicKey();
                Signature signet = Signature.getInstance("SHA256WITHRSA");
                signet.initVerify(pubKey);
                signet.update(inDataBytes);
                res = signet.verify(e);
            }
        } catch (InvalidKeyException var12) {
        	LogUtil.debug(this.getClass(), "verify", "Invalid key exception:%s",var12);
        } catch (NoSuchAlgorithmException var13) {
        	LogUtil.debug(this.getClass(), "verify", "No such algorithm exception:%s", var13);
        } catch (SignatureException var14) {
        	LogUtil.debug(this.getClass(), "verify", "Signature exception:%s", var14);
        } catch (SecurityException var15) {
        	LogUtil.debug(this.getClass(), "verify", "Security exception:%s", var15);
        }

        return res;
    }

    private X509Certificate getCertFromHexString(String hexCert) throws SecurityException {
        ByteArrayInputStream bIn = null;
        X509Certificate certobj = null;

        try {
            byte[] e = HexStringByte.hexToByte(hexCert.getBytes());
            CertificateFactory fact = CertificateFactory.getInstance("X.509");
            bIn = new ByteArrayInputStream(e);
            certobj = (X509Certificate)fact.generateCertificate(bIn);
            bIn.close();
            bIn = null;
        } catch (CertificateException var16) {
        	LogUtil.debug(this.getClass(), "verify", "Certificate exception:%s", var16);
        } catch (IOException var17) {
        	LogUtil.debug(this.getClass(), "verify", "IO exception:%s", var17);
        } finally {
            try {
                if(bIn != null) {
                    bIn.close();
                }
            } catch (IOException var15) {
                ;
            }

        }

        return certobj;
    }

    public static byte[] checkPEM(byte[] paramArrayOfByte) {
        String str1 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789/+= \r\n-";

        for(int localStringBuffer = 0; localStringBuffer < paramArrayOfByte.length; ++localStringBuffer) {
            if(str1.indexOf(paramArrayOfByte[localStringBuffer]) == -1) {
                return null;
            }
        }

        StringBuffer var5 = new StringBuffer(paramArrayOfByte.length);
        String str2 = new String(paramArrayOfByte);

        for(int j = 0; j < str2.length(); ++j) {
            if(str2.charAt(j) != 32 && str2.charAt(j) != 13 && str2.charAt(j) != 10) {
                var5.append(str2.charAt(j));
            }
        }

        return var5.toString().getBytes();
    }
    /**
     * 字符串转换为Map
     * @param respMsg
     * @return
     */
    public Map<String, String> coverString2Map(String dataStr) {
    	Map<String, String> resMap = new HashMap<String, String>();
    	if (dataStr == null || StringUtils.isBlank(dataStr) || dataStr.indexOf("&") < 0) {
			return resMap;
		}
        String[] resArr = StringUtils.split(dataStr, "&");
        for(int i = 0; i < resArr.length; ++i) {
            String data = resArr[i];
            int index = StringUtils.indexOf(data, '=');
            String nm = StringUtils.substring(data, 0, index);
            String val = StringUtils.substring(data, index + 1);
            resMap.put(nm, val);
        }

        return resMap;
    }
    /**
     * Map转换为字符串，去除null,merchantSign,serverSign,serverCert这些(不参与签名的)值，并按照字母排序
     * @param dataMap
     * @return
     */
    public String coverMap2String(Map<String, String> dataMap) {
    	if (dataMap == null || dataMap.isEmpty()) {
			return "";
		}
        TreeMap<String, String> tree = new TreeMap<String, String>();
        Iterator<Map.Entry<String,String>> it = dataMap.entrySet().iterator();

        while(it.hasNext()) {
            Entry<String,String> entry = (Entry<String,String>)it.next();
            if(!"merchantSign".equals(((String)entry.getKey()).trim()) && !"serverSign".equals(((String)entry.getKey()).trim()) 
            		&& !"serverCert".equals(((String)entry.getKey()).trim())) {
                if (entry.getValue() == null ||  "null".equals(entry.getValue()) || StringUtils.isBlank(entry.getValue())) {
                    continue;
                }
                tree.put(entry.getKey(), entry.getValue());
            }
        }
        it = tree.entrySet().iterator();
        
        StringBuffer dataBuffer = new StringBuffer();
        while(it.hasNext()) {
            Entry<String,String> entry = (Entry<String,String>)it.next();
            dataBuffer.append((String)entry.getKey() + "=" + (String)entry.getValue() + "&");
        }
        return dataBuffer.substring(0, dataBuffer.length() - 1);//去掉最后一个&
    }

    public void setService(String service) {
        this.service = service;
    }
    public String getService() {
        return service;
    }
    public String getCertInfo() {
        return this.hexCert;
    }
    
}
