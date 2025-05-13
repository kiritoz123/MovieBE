package com.hcmute.myanime.common;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Common
{
    public static class MessageRespone
    {
        // Movie
        public static String StorageMovieSuccess = "Storage movie success";
        public static String StorageMovieFail = "Storage movie fail";
        public static String UpdateMovieSuccess = "Update movie success";
    }

    public static class Function
    {
        public static <T> void revlist(List<T> list)
        {
            // base condition when the list size is 0
            if (list.size() <= 1 || list == null)
                return;


            T value = list.remove(0);

            // call the recursive function to reverse
            // the list after removing the first element
            revlist(list);

            // now after the rest of the list has been
            // reversed by the upper recursive call,
            // add the first value at the end
            list.add(value);
        }

        public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
            Map<String, String> query_pairs = new LinkedHashMap<>();
            String query = url.getQuery();
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            }
            return query_pairs;
        }
    }
}
