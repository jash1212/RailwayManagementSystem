package P1;

class Queue {
    int f;
    int size;
    int r;
    Train[] datas;

    Queue(int size) {
        f = -1;
        r = -1;
        this.size = size;
        datas = new Train[size];
    }

    void enqueue(Train data) {
        if (f - 1 == r || (f == 0 && r == size - 1)) {
            System.out.println("Size Full");
        } else {
            if (f == -1) {
                f = 0;
            }
            r = (r + 1) % size;
            datas[r] = data;
        }
    }

    Train dequeue() {
        if (f == -1 || r == -1) {
            return null;
        } else if (f == r) {
            Train data = datas[f];
            f = -1;
            r = -1;
            return data;

        } else {
            Train data = datas[f];
            f = (f + 1) % size;
            return data;
        }
    }

    boolean isEmpty() {
        if (f == -1 || r == -1) {
            return true;

        } else {
            return false;
        }
    }
}
