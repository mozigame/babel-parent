package com.babel.common.core.tools;

/**
 * 短链接生成
 * @author jinhe.chen
 *
 */
public class ShortUrlGenerator {
	 /**

     * @param args

     */

   public static void main(String[] args) {

      // 长连接： http://tech.sina.com.cn/i/2011-03-23/11285321288.shtml

      // 新浪解析后的短链接为： http://t.cn/h1jGSC

      String sLongUrl = "http://tech.sina.com.cn/i/2011-03-23/11285321288.shtml" ; // 3BD768E58042156E54626860E241E999

      String[] aResult = shortUrl (sLongUrl);

      // 打印出结果

      for ( int i = 0; i < aResult. length ; i++) {

          System. out .println( "[" + i + "]:::" + aResult[i]);

      }
      
      System.out.println(getShortUrl(sLongUrl));

   }



   public static String[] shortUrl(String url) {

      // 可以自定义生成 MD5 加密字符传前的混合 KEY

      String key = "jinhe" ;

      // 要使用生成 URL 的字符

      String[] chars = new String[]{          //要使用生成URL的字符   
              "a","b","c","d","e","f","g","h",   
              "i","j","k","l","m","n","o","p",   
              "q","r","s","t","u","v","w","x",   
              "y","z","0","1","2","3","4","5",   
              "6","7","8","9","A","B","C","D",   
              "E","F","G","H","I","J","K","L",   
              "M","N","O","P","Q","R","S","T",   
              "U","V","W","X","Y","Z"   
          };   

      // 对传入网址进行 MD5 加密

      String hex = Encript.md5(key + url);   
//      System.out.println(hex);
      int hexLen = hex.length();   
      int subHexLen = hexLen / 10;
      if(subHexLen>1){
    	  subHexLen=1;
      }
      String[] ShortStr = new String[3];   
         
      for (int i = 0; i < subHexLen; i++) {   
          String outChars = "";   
          int j = i + 1;   
          String subHex = hex.substring(i * 8, j * 8);   
          long idx = Long.valueOf("3FFFFFFF", 16) & Long.valueOf(subHex, 16);   
             
          for (int k = 0; k < 7; k++) {   
              int index = (int) (Long.valueOf("0000003D", 16) & idx);   
              outChars += chars[index];   
              idx = idx >> 5;   
          }   
          ShortStr[i] = outChars;   
      }   

      return ShortStr;

   }
   
   public static String getShortUrl(String url){
	   return shortUrl(url)[0];
   }
}
