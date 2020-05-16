package yf;
/* gh
 * 2020/4/26
 * 下午1:52
 */

import java.io.*;
import java.util.*;

public class Parser2 {
    Scanner scanner=null;
    PrintWriter pwVar=null;
    PrintWriter pwPro=null;
    PrintWriter pwErr=null;
    int level=0;//层次
    int rankOfVar=1;//变量的序号
    int lineNum=1;//行号
    static Set<String> words=new HashSet<>();
    List<String> lines=new ArrayList<>();
    int index=0;//lines的索引
    List<Variable> vars=new ArrayList<>();//变量表
    Stack<Pro> stackIn=new Stack<>();//入栈过程表
    Stack<Pro> stackOut=new Stack<>();//打印用过程表

    static {
        words.add("begin");
        words.add("end");
        words.add("integer");
        words.add("if");
        words.add("then");
        words.add("else");
        words.add("function");
        words.add("read");
        words.add("write");
    }

    public Parser2(File file) throws FileNotFoundException {
        String path = file.getAbsolutePath();
        int i = path.lastIndexOf(".");
        String parPath=path.substring(0,i);
        pwErr=new PrintWriter(new FileOutputStream(parPath+".err"));
        pwVar=new PrintWriter(new FileOutputStream(parPath+".var"));
        pwPro=new PrintWriter(new FileOutputStream(parPath+".pro"));
        scanner=new Scanner(new FileInputStream(file));

        while (scanner.hasNextLine()){
            lines.add(scanner.nextLine());
        }
    }

    String readNewWord(){//读取有效的下一行,没有则返回-1
        while (index<lines.size()){
            String nextLine=lines.get(index++);
            String str = nextLine.trim();
            String[] strs = str.split(" ");
            String fin=strs[0];
//            if(!(fin.equals("EOLN")||fin.equals("EOF"))){
//                return fin;
//            }
            if(fin.equals("EOLN")){//换行
                lineNum++;
            }else if(!fin.equals("EOF")){
                return fin;
            }
        }
        return "-1";
    }


    void writeErr(String err){
        pwErr.println("***LINE:"+lineNum+","+err);
        pwErr.flush();
    }

    /*
    String pname;
    String ptype;
    int plevl;
    int faddr;
    int laddr;
     */

    String writeP(Pro pro,int length){
        StringBuilder stb=new StringBuilder();

        int len1=length-pro.pname.length();
        for (int i = 0; i < len1; i++) {
            stb.append(" ");
        }
        stb.append(pro.pname).append("  ");

        int len2=length-pro.ptype.length();
        for (int i = 0; i < len2; i++) {
            stb.append(" ");
        }
        stb.append(pro.ptype).append("  ");

        String plvel=""+pro.plevl;
        int len3=length-plvel.length();
        for (int i = 0; i < len3; i++) {
            stb.append(" ");
        }
        stb.append(plvel).append("  ");

        String faddr=""+pro.faddr;
        int len4=length-faddr.length();
        for (int i = 0; i < len4; i++) {
            stb.append(" ");
        }
        stb.append(faddr).append("  ");



        String laddr=""+pro.laddr;
        int len6=length-laddr.length();
        for (int i = 0; i < len6; i++) {
            stb.append(" ");
        }
        stb.append(laddr);

        return stb.toString();
    }
    void writePro(int length){
        int len=length-5;
        StringBuilder stb1=new StringBuilder();
        for (int i = 0; i < len; i++) {
            stb1.append(" ");
        }
        StringBuilder stbHead=new StringBuilder();
        stbHead.append(stb1.toString()).append("pname").append("  ")
                .append(stb1.toString()).append("ptype").append("  ")
                .append(stb1.toString()).append("plevl").append("  ")
                .append(stb1.toString()).append("faddr").append("  ")
                .append(stb1.toString()).append("laddr");
        pwPro.println(stbHead.toString());
        pwPro.println();
        while (!stackOut.isEmpty()){
            Pro pro=stackOut.pop();
            for (Variable var : vars) {//找出第一个变量位置
                if (var.vlevl == pro.plevl) {
                    pro.faddr = var.vaddr;
                    break;
                }
            }
            for (int i = vars.size()-1; i >=0 ; i--) {//最后一个
                if(vars.get(i).vlevl ==pro.plevl){
                    pro.laddr =vars.get(i).vaddr;
                    break;
                }
            }
            pwPro.println(writeP(pro,length));
        }
        pwPro.flush();
    }

    void writeVar(int length){//length=16
        int len=length-5;
        StringBuilder stb1=new StringBuilder();
        for (int i = 0; i < len; i++) {
            stb1.append(" ");
        }
        StringBuilder stbHead=new StringBuilder();
        stbHead.append(stb1.toString()).append("vname").append("  ")
                .append(stb1.toString()).append("vproc").append("  ")
                .append(stb1.toString()).append("vkind").append("  ")
                .append(stb1.toString()).append("vtype").append("  ")
                .append(stb1.toString()).append("vlevl").append("  ")
                .append(stb1.toString()).append("vaddr");
        pwVar.println(stbHead.toString());
        pwVar.println();
        for (Variable variable : vars) {
            StringBuilder stb=new StringBuilder();

            int len1=length-variable.vname.length();
            for (int i = 0; i < len1; i++) {
                stb.append(" ");
            }
            stb.append(variable.vname).append("  ");

            int len2=length-variable.vproc.length();
            for (int i = 0; i < len2; i++) {
                stb.append(" ");
            }
            stb.append(variable.vproc).append("  ");

            String vkind=""+variable.vkind;
            int len3=length-vkind.length();
            for (int i = 0; i < len3; i++) {
                stb.append(" ");
            }
            stb.append(vkind).append("  ");

            int len4=length-variable.vtype.length();
            for (int i = 0; i < len4; i++) {
                stb.append(" ");
            }
            stb.append(variable.vtype).append("  ");

            String vlev=""+variable.vlevl;
            int len5=length-vlev.length();
            for (int i = 0; i < len5; i++) {
                stb.append(" ");
            }
            stb.append(vlev).append("  ");

            String vadr=""+variable.vaddr;
            int len6=length-vadr.length();
            for (int i = 0; i < len6; i++) {
                stb.append(" ");
            }
            stb.append(vadr);

            pwVar.println(stb.toString());
        }
        pwVar.flush();
    }

    int kindOfWord(String input){//返回-1表示出错
        if(words.contains(input)){//关键字
            return 1;
        }
        char ch=input.charAt(0);
        if(ch>='a'&&ch<='z'){//以字母开头，标识符
            return 10;
        }
        if(ch>='0'&&ch<='9'){//以数字开头，常数
            return 11;
        }
        switch (input){
            case "=":
                return 12;
            case "<>":
                return 13;
            case "<=":
                return 14;
            case "<":
                return 15;
            case ">=":
                return 16;
            case ">":
                return 17;
            case "-":
                return 18;
            case "*":
                return 19;
            case ":=":
                return 20;
            case "(":
                return 21;
            case ")":
                return 22;
            case ";":
                return 23;
            default:
                return -1;
        }
    }

    boolean varIsExist(Variable var){
        for (Variable variable : vars) {
            if(var.equals(variable)){
                return true;
            }
        }
        return false;
    }


    void prgma(){//1
        pprgma();
        writePro(8);
        writeVar(8);
    }

    boolean pprgma(){//2
        String be=readNewWord();
        Pro main=new Pro("main","void",level++);
        stackIn.push(main);
        if(be.equals("begin")){
            exp_state_table();
            String be1=readNewWord();
            if(be1.equals(";")){
                exe_state_table();
                String be3=readNewWord();
                if(be3.equals("end")){
                    Pro pro=stackIn.pop();
                    stackOut.push(pro);
                    return true;
                }else {
                    writeErr("main过程end结束标志错误");
                    return false;
                }
            }else {
                writeErr("缺少\";\"错误");
                return false;
            }
        }else {
            writeErr("main过程的begin开始标志错误");
            return false;
        }
    }

    void exp_state_table(){//3
        exp_state();
        mid_exp_state_table();
    }

    void mid_exp_state_table(){//4
        String str1=readNewWord();
        if(str1.equals(";")){
            String str2=readNewWord();
            if(!str2.equals("integer")){//bug1 需要向前判断一步,看下一步是说明语句还是执行语句,并需要回退3步
                index-=3;
                lineNum--;
            }else {
                index--;
                exp_state();
                mid_exp_state_table();
            }
        }else {
            index--;
        }
    }

    boolean exp_state(){//5
        String str1=readNewWord();
        if(str1.equals("integer")){
            mid_exp_state();
            return true;
        }else {
            writeErr("变量或过程定义缺少integer");
            return false;
        }
    }


    boolean mid_exp_state(){//6
        String str1=readNewWord();
        int num1=kindOfWord(str1);
        if(num1==10){//标识符
            Variable var=new Variable(str1,stackIn.peek().pname,0,"integer",stackIn.peek().plevl,rankOfVar++);
            if(varIsExist(var)){//存在该变量
                writeErr(str1+"变量已经定义过,不可重复定义");
                return false;
            }else {//未定义过
                vars.add(var);
                return true;
            }
        }
        if(str1.equals("function")){
            String str2=readNewWord();
            Pro pro=new Pro(str2,"integer",level++);
            stackIn.push(pro);
            int num2=kindOfWord(str2);
            if(num2==10){
                String str3=readNewWord();
                if(str3.equals("(")){
                    String str4=readNewWord();//形参
//                    Variable var=new Variable(str4,stackIn.peek().pname,1,"integer",stackIn.peek().plevl,rankOfVar++);
//                    vars.add(var);
                    int num4=kindOfWord(str4);
                    if(num4==10){
                        String str5=readNewWord();
                        if(str5.equals(")")){
                            String str6=readNewWord();
                            if(str6.equals(";")){
                                fun_body();
                                return true;
                            }else {
                                writeErr("缺少\";\"错误");
                                return false;
                            }
                        }else {
                            writeErr("缺少\")\"错误");
                            return false;
                        }
                    }else {
                        writeErr("缺少形参错误");
                        return false;
                    }
                }else {
                    writeErr("缺少\"(\"错误");
                    return false;
                }
            }else {
                writeErr("过程名错误");
                return false;
            }
        }
        writeErr("不合法的说明语句");
        return false;
    }

    boolean fun_body(){//7
        String str1=readNewWord();
        if(str1.equals("begin")){
            exp_state_table();//说明语句
            String str2=readNewWord();
            if(str2.equals(";")){
                exe_state_table();//执行语句
                String str3=readNewWord();
                if(str3.equals("end")){
                    Pro pro=stackIn.pop();
                    stackOut.push(pro);
                    return true;
                }else {
                    writeErr("过程缺少end结束标志");
                    return false;
                }
            }else {
                writeErr("缺少\";\"错误");
                return false;
            }
        }else {
            writeErr("过程体缺少begin开始标志错误");
            return false;
        }
    }

    void exe_state_table(){//8
        exe_state();
        mid_exe_state_table();
    }

    void mid_exe_state_table(){//9
        String str1=readNewWord();
        if(str1.equals(";")){
            exe_state();
            mid_exe_state_table();
        }else {
            index--;
        }
    }

    boolean exe_state(){//10
        String str1=readNewWord();
        int num1=kindOfWord(str1);
        if(num1==10){//标识符
            String str2=readNewWord();
            if(str2.equals(":=")){
                ari_expression();
                return true;
            }else {
                writeErr("缺少赋值符号\":=\"");
                return false;
            }
        }
        if(str1.equals("read")){//read
            String str2=readNewWord();
            if(str2.equals("(")){
                String str3=readNewWord();
                int num3=kindOfWord(str3);
                if(num3==10){
                    Variable var=new Variable(str3,stackIn.peek().plevl);
                    if(!varIsExist(var)){//存在该变量的定义,可以使用
                        writeErr(str3+"变量未定义");
                    }
                    String str4=readNewWord();
                    if(str4.equals(")")){
                        return true;
                    }else {
                        writeErr("缺少\")\"错误");
                        return false;
                    }
                }else {
                    writeErr("read函数调用无正确传入变量");
                    return false;
                }
            }else {
                writeErr("缺少\"(\"错误");
                return false;
            }
        }
        if(str1.equals("write")){//write
            String str2=readNewWord();
            if(str2.equals("(")){
                String str3=readNewWord();
                int num3=kindOfWord(str3);
                if(num3==10) {
                    Variable var = new Variable(str3, stackIn.peek().plevl);
                    if (!varIsExist(var)) {//存在该变量的定义,可以使用
                        writeErr(str3 + "变量未定义");
                    }
                    String str4 = readNewWord();
                    if (str4.equals(")")) {
                        return true;
                    } else {
                        writeErr("缺少\")\"错误");
                        return false;
                    }
                }else {
                    writeErr("read函数调用无正确传入变量");
                    return false;
                }
            }else {
                writeErr("缺少\"(\"错误");
                return false;
            }
        }
        if(str1.equals("if")) {//if
            con_expression();
            String str2 = readNewWord();
            if (str2.equals("then")) {
                exe_state();
                String str3 = readNewWord();
                if (str3.equals("else")) {
                    exe_state();
                    return true;
                } else {
                    writeErr("缺少\"else\"关键字");
                    return false;
                }
            } else {
                writeErr("缺少\"then\"关键字");
                return false;
            }
        }
        if(str1.equals("end")){//bug 当遇到end时,被当作运算处理,需要回退
            index--;
        }
        return false;
    }


    void ari_expression(){//11
        pro();
        mid_ari_expression();
    }

    void mid_ari_expression(){//12
        String str1=readNewWord();
        if(str1.equals("-")){
            pro();
            mid_ari_expression();
        }else {
            index--;
        }
    }

    void pro(){//13
        factor();
        mid_pro();
    }

    void mid_pro(){//14
        String str1=readNewWord();
        if(str1.equals("*")){
            factor();
            mid_pro();
        }else {
            index--;
        }
    }

    boolean factor(){//15
        String str1=readNewWord();
        int num1=kindOfWord(str1);
        if(num1==10){//标识符
            mid_factor();
            return true;
        }else if(num1==11){//常数
            return true;
        }else {
            writeErr("算术表达式格式错误");
            return false;
        }
    }

    void mid_factor(){//16
        String str1=readNewWord();
        if(str1.equals("(")){
            String strn=readNewWord();
            Variable var=new Variable(strn,stackIn.peek().plevl);
            if(!varIsExist(var)){
                writeErr(strn+"变量未定义");
            }
            index--;
            ari_expression();
            String str2=readNewWord();
            if(!str2.equals(")")){
                writeErr("缺少\")\"错误");
            }
        }else {
            index--;
        }
    }

    void con_expression(){//17
        ari_expression();
        con_op();
        ari_expression();
    }

    boolean con_op(){//18
        String str1=readNewWord();
        int num1=kindOfWord(str1);
        if(num1>=12&&num1<=17){
            return true;
        }else {
            writeErr("错误的运算符");
            return false;
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        File file=new File("test.dyd");
        Parser2 parser2=new Parser2(file);
        parser2.prgma();
    }
}
