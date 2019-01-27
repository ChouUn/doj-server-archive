-- !Ups

CREATE TABLE "problem"
(
  id            SERIAL PRIMARY KEY,
  voj           VARCHAR(16)                                  NOT NULL,
  vid           VARCHAR(16)                                  NOT NULL,

  status        INTEGER   DEFAULT 0                          NOT NULL,
  config        JSONB     DEFAULT '{}'                       NOT NULL,

  title         VARCHAR(256)                                 NOT NULL,
  description   TEXT                                         NOT NULL,
  input_format  TEXT,
  output_format TEXT,
  example       TEXT,
  hint          TEXT,
  source        VARCHAR(128),
  author        VARCHAR(32),

  created_by    INTEGER,
  created_at    TIMESTAMP DEFAULT (now() at time zone 'utc') NOT NULL,

  CONSTRAINT problem_voj_vid_uniq
    UNIQUE (voj, vid),

  CONSTRAINT problem_created_by_fk
    FOREIGN KEY (created_by)
      REFERENCES "user" (id)
);

-- !Downs

DROP TABLE problem;
