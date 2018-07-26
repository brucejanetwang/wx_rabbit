package com.northtech.WeChat.wxmetainfo.utils;

import com.northtech.Common.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DesSecretUtil {

    private static String keyCode="bsecpark";//秘钥可以任意改，只要总长度是8个字节就行
    private static byte[] iv = { '5','7','8','7','5','4','5','4'};

    public static String encryptDES(String encryptString)
            throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(keyCode.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes("utf-8"));
        return Base64.encode(encryptedData);
    }
    public static String decryptDES(String decryptString)
            throws Exception {
        byte[] byteMi = Base64.decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(keyCode.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);

        return new String(decryptedData ,"utf-8");
    }
    /**将二进制转换成16进制
     * @param buf
     * @return  String
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String encryptString   = "我 bruce wang 账单信息如下";
        try {
            String convert  = DesSecretUtil.encryptDES(encryptString);
            System.out.println(convert);
            String newstr = DesSecretUtil.decryptDES(convert);
            System.out.println(newstr);
            //System.out.println(Encodes.urlEncode(convert));
            System.out.println( DesSecretUtil.decryptDES("aLAb0kr+ecwuJW1VCCiwEEmK50yoXY1l/cxvV04MyYmGybEY7XDNJkz+auVk FKbcAG1Cs9FduAmtbuBiFJBR+D5bpMyD5em2UTYzp53YFGEDKIDxqpUORWek YvOJBLVYoEQL4VYA7QTzwqIaW8cVhnF9/iTysRpa"));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}