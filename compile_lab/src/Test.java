
/* gh
 * 2020/4/25
 * 下午10:42
 */

import java.io.FileInputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        FileInputStream fi=new FileInputStream("right.pas");
        char ch='\0';
        int ch1;
        while ((ch1=fi.read())!=-1){
            ch=(char)ch1;
            if(ch==' ')
                continue;
            System.out.print(ch);
        }

    }
}
