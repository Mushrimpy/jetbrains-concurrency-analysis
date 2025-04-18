public class RecursiveCall {
    public void A() {
        B();
    }
    public void B() {
        B();
        C();
    }
    public void C() {
    }
}