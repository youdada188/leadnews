-- App 用户表
CREATE TABLE IF NOT EXISTS ap_user (
                                       id       INTEGER PRIMARY KEY,
                                       name     TEXT,
                                       password TEXT,
                                       phone    TEXT,
                                       image    TEXT,
                                       sex      INTEGER,
                                       status   INTEGER
);

-- 自媒体用户表
CREATE TABLE IF NOT EXISTS wm_user (
                                       id       INTEGER PRIMARY KEY,
                                       name     TEXT,
                                       password TEXT,
                                       nickname TEXT,
                                       image    TEXT,
                                       phone    TEXT,
                                       status   INTEGER
);

-- 频道表
CREATE TABLE IF NOT EXISTS channel (
                                       id           INTEGER PRIMARY KEY,
                                       name         TEXT,
                                       description  TEXT,
                                       is_default   INTEGER,
                                       status       INTEGER,
                                       ord          INTEGER,
                                       created_time TEXT
);

-- 文章表
CREATE TABLE IF NOT EXISTS news (
                                    id           INTEGER PRIMARY KEY,
                                    title        TEXT,
                                    author_id    INTEGER,
                                    author_name  TEXT,
                                    channel_id   INTEGER,
                                    channel_name TEXT,
                                    type         INTEGER,
                                    images       TEXT,
                                    labels       TEXT,
                                    likes        INTEGER,
                                    collection   INTEGER,
                                    comment      INTEGER,
                                    views        INTEGER,
                                    forward      INTEGER,
                                    created_time TEXT,
                                    publish_time TEXT,
                                    static_url   TEXT,
                                    content      TEXT,
                                    status       INTEGER
);

-- 素材表
CREATE TABLE IF NOT EXISTS material (
                                        id            INTEGER PRIMARY KEY,
                                        user_id       INTEGER,
                                        url           TEXT,
                                        type          INTEGER,
                                        is_collection INTEGER,
                                        created_time  TEXT
);

-- 文章-素材关联表
CREATE TABLE IF NOT EXISTS wm_news_material (
                                                id          INTEGER PRIMARY KEY,
                                                material_id INTEGER,
                                                news_id     INTEGER,
                                                type        INTEGER,
                                                ord         INTEGER
);