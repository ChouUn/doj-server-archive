
# --- !Ups
CREATE TABLE core_problem (
    id               SERIAL                   NOT NULL
        CONSTRAINT core_problem_pkey
        PRIMARY KEY,

    created_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITH TIME ZONE NOT NULL,

    voj              VARCHAR(16),
    vid              VARCHAR(16)              NOT NULL,

    author           VARCHAR(32),
    provider_id      INTEGER
        CONSTRAINT core_problem_provider_id_fk_user_id
        REFERENCES user
            DEFERRABLE INITIALLY DEFERRED,
    private          BOOLEAN                  NOT NULL,

    type             INTEGER                  NOT NULL,
    time_limit       INTEGER                  NOT NULL,
    memory_limit     INTEGER                  NOT NULL,

    title            VARCHAR(128)             NOT NULL,
    description      TEXT                     NOT NULL,
    input_format     TEXT                     NOT NULL,
    output_format    TEXT                     NOT NULL,
    example          TEXT,
    subtask_and_hint TEXT,
    source           VARCHAR(128),

    CONSTRAINT core_problem_voj_vid_uniq
    UNIQUE (voj, vid)
);

CREATE INDEX core_problem_voj_vid_idx
    ON core_problem (voj, vid);

CREATE INDEX core_problem_provider_id_idx
    ON core_problem (provider_id);


# --- !Downs

DROP INDEX core_problem_provider_id_idx;

DROP INDEX core_problem_voj_vid_idx;

DROP TABLE core_problem;
