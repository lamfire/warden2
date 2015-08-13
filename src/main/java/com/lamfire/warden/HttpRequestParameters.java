package com.lamfire.warden;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface HttpRequestParameters {
    public Map<String,List<String>> all();
    public Set<String> names();
    public byte[] asBytes();
    public String asString();
    public String asString(Charset charset);
    public String asString(String charset);
    public String get(String name);
    public List<String> gets(String name);
    public int getInt(String name);
    public List<Integer> getInts(String name);
    public long getLong(String name);
    public List<Long> getLongs(String name);
    public float getFloat(String name);
    public List<Float> getFloats(String name);
    public double getDouble(String name);
    public List<Double> getDoubles(String name);
    public String getString(String name);
    public List<String> getStrings(String name);
}
