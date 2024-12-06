package com.noobisoftcontrolcenter.tokemon.model;

import java.util.List;

public class Metadata {
    private String name;
    private String description;
    private String image;
    private String type;
    private Properties properties;
    private List<File> files;
    private Localization localization;

    public static class Properties {
        private String game;

        public String getGame() {
            return game;
        }

        public void setGame(String game) {
            this.game = game;
        }
    }

    public static class File {
        private String uri;
        private String type;
        private boolean isDefaultFile;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isDefaultFile() {
            return isDefaultFile;
        }

        public void setDefaultFile(boolean defaultFile) {
            isDefaultFile = defaultFile;
        }
    }

    public static class Localization {
        private String uri;
        private String defaultLocale;
        private List<String> locales;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getDefaultLocale() {
            return defaultLocale;
        }

        public void setDefaultLocale(String defaultLocale) {
            this.defaultLocale = defaultLocale;
        }

        public List<String> getLocales() {
            return locales;
        }

        public void setLocales(List<String> locales) {
            this.locales = locales;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public Localization getLocalization() {
        return localization;
    }

    public void setLocalization(Localization localization) {
        this.localization = localization;
    }
}
