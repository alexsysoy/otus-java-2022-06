public class CommonResource {
    private int count;
    private boolean isFirstThreadFinish;
    private boolean inverse = false;

    public CommonResource(int count, boolean isFirstThreadFinish) {
        this.count = count;
        this.isFirstThreadFinish = isFirstThreadFinish;
    }

    public int getCount() {
        return count;
    }

    public boolean isFirstThreadFinish() {
        return isFirstThreadFinish;
    }

    public void setFirstThreadFinish(boolean firstThreadFinish) {
        isFirstThreadFinish = firstThreadFinish;
    }

    public void countMoveToNext() {
        if (!inverse) {
            count++;
        } else {
            count--;
        }
        isItNecessaryToInvert();
    }

    private void isItNecessaryToInvert() {
        if (!isFirstThreadFinish && count == 12) {
            inverse = true;
        }
        if (!isFirstThreadFinish && count == 1) {
            inverse = false;
        }
    }

    @Override
    public String toString() {
        return "CommonResource{" +
                "count=" + count +
                ", isFirstThreadFinish=" + isFirstThreadFinish +
                '}';
    }
}
