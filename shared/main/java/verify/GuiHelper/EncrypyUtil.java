package verify.GuiHelper;
 
import sun.misc.BASE64Decoder;
 
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * [url=home.php?mod=space&uid=1248337]@version[/url] 1.0.0
 * @Description
 * @createTime 2022年12月01日 17:55:00
 */
public class EncrypyUtil {

 
 
    /**
     * 加密key 可替换 长度16
     */
    private static  String KEY = "nmslcnmdsbntmd56";
 
    /**
     * 加密方式 可替换 具体百度 PKCS7 等加密方式 java这个不需要额外插件
     */
    private static  String ALGORITHM = "AES/ECB/PKCS5Padding";
 
    /**
     * 先加密成base64
     * [url=home.php?mod=space&uid=952169]@Param[/url] bytes 字节
     * [url=home.php?mod=space&uid=155549]@Return[/url] String
     */
    public static String base64Encode(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }
 

 
    /**
     * 对字符串的字节数组进行Aes加密
     * @param content 加密字符串
     * @param encryptKey 加密key
     * @return  byte[]
     * @throws Exception ex
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        // AES加密
        KeyGenerator kgen = KeyGenerator.getInstance("AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 密钥加密
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
 
        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }
 
    /**
     * 对字符串的字节数组进行Aes加密
     * @param content 加密字符串
     * @param encryptKey 加密key
     * @return  byte[]
     * @throws Exception ex
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        // 加密后 在base64 转一下
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }
 
    /**
     * aes的解密
     * @param encryptBytes 加密字节
     * @param decryptKey key
     * @return 字符串
     * @throws Exception ex
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
 
        return new String(decryptBytes);
    }

 
    /**
     * 对外使用的加密
     * @param object 加密对象 需要对象重写ToString()方法
     * @return String
     * @throws Exception 异常
     */
    public static String encode(Object object) throws Exception {
        String content = object.toString();
        return aesEncrypt(content, KEY);
    }
 


 
}