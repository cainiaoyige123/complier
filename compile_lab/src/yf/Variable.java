package yf;
/* gh
 * 2020/4/26
 * 下午6:07
 */

import java.util.Objects;

public class Variable {
    String vname;
    String vproc;
    int vkind;//0:变量　1:形参
    String vtype;
    int vlevl;
    int vaddr;

    public Variable(String vname, String vproc, int vkind, String vtype, int vlev, int vadr) {
        this.vname = vname;
        this.vproc = vproc;
        this.vkind = vkind;
        this.vtype = vtype;
        this.vlevl = vlev;
        this.vaddr = vadr;
    }

    @Override
    public String toString() {
        return vname+" "+vproc+" "+vkind+" "+vtype+" "+ vlevl +" "+ vaddr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return vlevl == variable.vlevl &&
                Objects.equals(vname, variable.vname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vname, vlevl);
    }

    public Variable(String vname, int vlev) {
        this.vname = vname;
        this.vlevl = vlev;
    }
}
