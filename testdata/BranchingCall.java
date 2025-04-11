public class BrachingCall {
    public void source(boolean condition) {
        if (condition) {
            A();
        } else {
            B();
        }
    }
    private void A() {
        target();
    }
    private void B() {
        target();
    }
    public void target() {
    }
}