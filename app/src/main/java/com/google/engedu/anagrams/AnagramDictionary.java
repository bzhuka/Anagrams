/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    //variables I made
    int wordLength = DEFAULT_WORD_LENGTH;
    ArrayList<String> wordList = new ArrayList<String>();
    HashSet<String> wordSet = new HashSet<String>();
    HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = sortLetters(word);
            ArrayList<String> temp = lettersToWord.get(sortedWord);
            if (temp == null) {
                temp = new ArrayList<String>();
                temp.add(word);
                lettersToWord.put(sortedWord, temp);
            } else {
                temp.add(word);
            }
            int sizeOfWord = word.length();
            ArrayList<String> temp2 = lettersToWord.get(sortedWord);
            temp2 = sizeToWords.get(sizeOfWord);
            if (temp2 == null) {
                temp2 = new ArrayList<String>();
                temp2.add(word);
                sizeToWords.put(sizeOfWord, temp2);
            } else {
                temp2.add(word);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if (!wordSet.contains(word)) {
            return false;
        }
        for (int i = 0; i <= word.length() - base.length(); i++) {
            if (word.substring(i, i + base.length()).equals(base)) {
                return false;
            }
        }
        return true;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for (String temp: wordList) {
            if (temp.length() == targetWord.length()) {
                if (sortLetters(temp).equals(sortLetters(targetWord))) {
                    result.add(temp);
                }
            }
        }
        return result;
    }

    private String sortLetters(String word) {
        char[] arr = word.toCharArray();
        Arrays.sort(arr);
        String answer = new String(arr);
        return answer;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char alpha = 'a'; alpha <= 'z'; alpha++) {
            String sortedTemp = sortLetters(word + alpha);
            ArrayList<String> lettersList = lettersToWord.get(sortedTemp);
            if (lettersList != null) {
                for(String temp: lettersList) {
                    if (isGoodWord(temp, word)) {
                        result.add(temp);
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> asdf = sizeToWords.get(wordLength);
        int index = random.nextInt(asdf.size());
        String starterString = asdf.get(index);
        while(getAnagramsWithOneMoreLetter(starterString).size() < MIN_NUM_ANAGRAMS) {
            index++;
            starterString = asdf.get(index % asdf.size());
        }
        wordLength++;
        return starterString;
    }
}
