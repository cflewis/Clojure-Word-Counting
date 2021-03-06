(ns wordcounter
    (:import (java.io BufferedReader FileReader))
    (:import (org.apache.commons.lang StringEscapeUtils)))

(def words '("one" "two" "three" "four" "four" "four" "four"))

(defn file-lines [filename]
    (line-seq (BufferedReader. (FileReader. filename))))
    
(defn decode-html [string]
    (StringEscapeUtils/unescapeHtml string))
    
(defn to-lower-case [token-string]
    (.toLowerCase token-string))
        
(defn map-count-reduce
    "Takes a map of keys -> numbers, and adds one to the key value of key string."
    [map1 keystring]
    ;; This works by taking the map, the key, then changing the value
    ;; adding 1 to the value that's there, or zero if there's nothing
    (assoc map1 keystring (+ (if (map1 keystring) (map1 keystring) 0) 1)))

(defn count-words
    "Counts words in a list, returning a map of the word and the number of 
    occurences."
    [wordlist]
    (if (empty? wordlist)
        {}
    (reduce map-count-reduce {} (map to-lower-case wordlist))))

(defn count-words-line
    "Counts the number of words in a sequence of lines"
    [lines]
    (if (empty? lines)
        {}
    (reduce (partial merge-with +) (pmap count-words (pmap (partial re-seq #"\w+") (pmap decode-html lines))))))

(defn count-file-words
    "Counts the words in a given file"
    [filename]
    (count-words-line (file-lines filename)))

(time (count-file-words "posts-1000.xml"))