package com.hackathon.KCThack.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Component
public class ProfanityDictionary {

    private static final Logger log = LoggerFactory.getLogger(ProfanityDictionary.class);

    private final Set<String> bannedWords = new HashSet<>();

    @Value("${profanity.dictionary.path}")
    private String dictionaryPath;

    @Value("${profanity.dictionary.fallback-to-classpath:true}")
    private boolean fallbackToClasspath;

    @PostConstruct
    public void load() throws IOException {
        Resource resource = new FileSystemResource(dictionaryPath);
        if (resource.exists()) {
            log.info("Загружаем внешний словарь: {}", dictionaryPath);
            loadFromResource(resource);
        } else {
            log.warn("Внешний файл словаря не найден: {}", dictionaryPath);
            if (fallbackToClasspath) {
                resource = new ClassPathResource("banned-words.txt");
                log.info("Используем встроенный словарь из classpath");
                loadFromResource(resource);
            } else {
                throw new IOException("Файл словаря обязателен: " + dictionaryPath);
            }
        }
    }

    private void loadFromResource(Resource resource) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase();
                if (!word.isEmpty() && !word.startsWith("#")) { // поддержка комментариев
                    bannedWords.add(word);
                }
            }
        }
        log.info("Загружено {} запрещённых слов", bannedWords.size());
    }

    /**
     * Проверяет, содержит ли текст любое запрещённое слово (без пробелов, регистронезависимо).
     */
    public boolean containsBannedWord(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }
        // Убираем все пробельные символы и приводим к нижнему регистру
        String cleaned = text.replaceAll("\\s+", "").toLowerCase();
        for (String banned : bannedWords) {
            if (cleaned.contains(banned)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Возвращает первое найденное запрещённое слово или null.
     */
    public String findBannedWord(String text) {
        if (text == null || text.isBlank()) return null;
        String cleaned = text.replaceAll("\\s+", "").toLowerCase();
        for (String banned : bannedWords) {
            if (cleaned.contains(banned)) {
                return banned;
            }
        }
        return null;
    }
}