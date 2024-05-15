package es.boffmedia.teras.objects;

import es.boffmedia.teras.objects.post.SmartRotomPost;

public class TrainerDefeatMoney extends SmartRotomPost {
    int money;

    public TrainerDefeatMoney(String uuid, int money) {
        super();
        this.money = money;
        this.uuid = uuid;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
