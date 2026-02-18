public class PandaList {
    public Panda item;
    public PandaList rest;

    public PandaList(Panda p) {
        this.item = p;
        this.rest = null;
    }
}
