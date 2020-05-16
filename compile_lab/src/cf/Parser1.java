package cf;
/* gh
 * 2020/4/25
 * 下午11:04
 */

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Parser1 {
    private static String BEGIN = "01";
    private static String END = "02";
    private static String INTEGER = "03";
    private static String IF = "04";
    private static String THEN = "05";
    private static String ELSE = "06";
    private static String FUNCTION = "07";
    private static String READ = "08";
    private static String WRITE = "09";
    private static String TAG = "10";//标识符
    private static String CONST = "11";
    private static String EQUAL = "12";//相等
    private static String NE = "13";
    private static String LE = "14";
    private static String L = "15";
    private static String GE = "16";
    private static String G = "17";
    private static String SUB = "18";
    private static String MUL = "19";
    private static String ASS = "20";//赋值
    private static String LBRK = "21";//左括号
    private static String RBRK = "22";//右括号
    private static String SEM = "23";//分号;
    private static String EOLN = "24";//换行符
    private static String EOF = "25";//文件结尾
    private PushbackInputStream inputStream = null;
    private PrintWriter pwErr = null;
    private PrintWriter pwDyd = null;
    private char next = '\0';
    private StringBuilder tempStr = new StringBuilder();
    private static Map<String, String> proWord = new HashMap<>();//保留字集合

    static {
        proWord.put("begin", BEGIN);
        proWord.put("end", END);
        proWord.put("integer", INTEGER);
        proWord.put("if", IF);
        proWord.put("then", THEN);
        proWord.put("else", ELSE);
        proWord.put("function", FUNCTION);
        proWord.put("read", READ);
        proWord.put("write", WRITE);
    }

    private Parser1(File file) throws FileNotFoundException {
        String path = file.getAbsolutePath();
        int index = path.lastIndexOf(".");
        String parPath = path.substring(0, index);
        String errFile = parPath + ".err1";
        String dydFile = parPath + ".dyd";
        pwDyd = new PrintWriter(new OutputStreamWriter(new FileOutputStream(dydFile)));
        pwErr = new PrintWriter(new OutputStreamWriter(new FileOutputStream(errFile)));
        this.inputStream = new PushbackInputStream(new FileInputStream(file));
    }

    void writeDyd(String letter, String number) {
        int length = letter.length();
        int num = 16 - length;
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < num; i++)
            stb.append(" ");
        stb.append(letter);
        stb.append(" ").append(number);
        pwDyd.println(stb.toString());
        pwDyd.flush();
    }

    void writeErr(int line, String err) {
        pwErr.println("***LINE:" + line + "  " + err);
        pwErr.flush();
    }

    void skip() throws IOException {
        if (next == '\0') {
//            next=(char)inputStream.read();
            while ((next = (char) inputStream.read()) == ' ') ;
        } else {
            while (next == ' ') {
                next = (char) inputStream.read();
            }
        }
    }

    boolean end() {
        try {
            int num=inputStream.read();
            inputStream.unread(num);
            if(num==-1){//结束
                return true;
            }else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    void parse() throws IOException {//不用unread
        int lineNum = 1;
        while (true) {
            skip();
            if (isDigit(next)) {//常数
                while (isDigit(next)) {
                    tempStr.append(next);
                    next = (char) inputStream.read();
                }
                writeDyd(tempStr.toString(), CONST);
                tempStr = new StringBuilder();
                continue;
            }

            if (isLetter(next)) {//标识符或保留字
                boolean flag=false;
                while (isLetter(next) || isDigit(next)) {
                    tempStr.append(next);
                    flag=end();
                    next = (char) inputStream.read();
                }
                if (proWord.containsKey(tempStr.toString())) {//保留字
                    writeDyd(tempStr.toString(), proWord.get(tempStr.toString()));
                } else {//标识符
                    int len=tempStr.toString().length();
                    if(len>16){
                        writeErr(lineNum,"\""+tempStr.toString()+"\"标识符过长,应该不大于16");
                        tempStr=new StringBuilder(tempStr.subSequence(0,16));//超过16,则截取前16,并报错
                    }
                    writeDyd(tempStr.toString(), TAG);
                }
                tempStr = new StringBuilder();
                if (flag){
                    tempStr.append("EOF");
                    writeDyd(tempStr.toString(),EOF);
                    break;
                }
                continue;
            }

            if (next == '=') {
                tempStr.append(next);
                writeDyd(tempStr.toString(), EQUAL);
                tempStr = new StringBuilder();
                next = (char) inputStream.read();
                continue;
            }

            if (next == '-') {
                tempStr.append(next);
                writeDyd(tempStr.toString(), SUB);
                tempStr = new StringBuilder();
                next = (char) inputStream.read();
                continue;
            }

            if (next == '*') {
                tempStr.append(next);
                writeDyd(tempStr.toString(), MUL);
                tempStr = new StringBuilder();
                next = (char) inputStream.read();
                continue;
            }

            if (next == '(') {
                tempStr.append(next);
                writeDyd(tempStr.toString(), LBRK);
                tempStr = new StringBuilder();
                next = (char) inputStream.read();
                continue;
            }

            if (next == ')') {
                tempStr.append(next);
                writeDyd(tempStr.toString(), RBRK);
                tempStr = new StringBuilder();
                next = (char) inputStream.read();
                continue;
            }

            if (next == '<') {
                tempStr.append(next);
                next = (char) inputStream.read();
                if (next == '=') {// <=
                    tempStr.append(next);
                    writeDyd(tempStr.toString(), LE);
                    next = (char) inputStream.read();
                } else if (next == '>') {// <>
                    tempStr.append(next);
                    writeDyd(tempStr.toString(), NE);
                    next = (char) inputStream.read();
                } else {// <
                    writeDyd(tempStr.toString(), L);
                }
                tempStr = new StringBuilder();
                continue;
            }

            if (next == '>') {
                tempStr.append(next);
                next = (char) inputStream.read();
                if (next == '=') {// >=
                    tempStr.append(next);
                    writeDyd(tempStr.toString(), GE);
                    next = (char) inputStream.read();
                } else {
                    writeDyd(tempStr.toString(), G);
                }
                tempStr = new StringBuilder();
                continue;
            }

            if (next == ':') {
                tempStr.append(next);
                next = (char) inputStream.read();
                if (next == '=') {// :=
                    tempStr.append(next);
                    writeDyd(tempStr.toString(), ASS);
                    next = (char) inputStream.read();
                } else {
                    //TODO　出错处理
                    writeErr(lineNum, "赋值语句缺少\":=\"");
                }
                tempStr = new StringBuilder();
                continue;
            }

            if (next == ';') {
                tempStr.append(next);
                writeDyd(tempStr.toString(), SEM);
                tempStr = new StringBuilder();
                next = (char) inputStream.read();
                continue;
            }
            if (next == '\n') {//换行
                tempStr.append("EOLN");
                writeDyd(tempStr.toString(), EOLN);
                next = (char) inputStream.read();
                tempStr = new StringBuilder();
                lineNum++;
                continue;
            }
            //TODO　出错处理
            writeErr(lineNum, "此语言不含字符\""+next+"\"");
            next = (char) inputStream.read();
        }
    }


    boolean isLetter(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static void main(String[] args) throws IOException {
        Parser1 parser=new Parser1(new File("right.pas"));
        parser.parse();
    }
}
