package yf;
/* gh
 * 2020/4/26
 * 下午9:58
 */

public class Pro {
    String pname;
    String ptype;
    int plevl;
    int faddr;
    int laddr;

    @Override
    public String toString() {
        return pname+" "+ptype+" "+ plevl +" "+ faddr +" "+ laddr;
    }

    public Pro(String pname, String ptype, int plev) {
        this.pname = pname;
        this.ptype = ptype;
        this.plevl = plev;
    }

    public void setFaddr(int faddr) {
        this.faddr = faddr;
    }

    public void setLaddr(int laddr) {
        this.laddr = laddr;
    }
}
