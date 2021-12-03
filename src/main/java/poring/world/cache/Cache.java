package poring.world.cache;

import java.util.HashMap;

public class Cache extends HashMap<String, String> {

    public int gets = 0;

    public int howManyGets() {
        return this.gets;
    }

    @Override
    public void clear() {
        System.out.println("Cache used: " + this.gets);
        this.gets = 0;
        super.clear();
    }
}
