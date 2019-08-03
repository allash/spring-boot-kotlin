CREATE TABLE "user" (
    "id" UUID NOT NULL,
    "email" TEXT NOT NULL,
    "password" TEXT NOT NULL,
    "first_name" TEXT NOT NULL,
    "last_name" TEXT NOT NULL,
    "created_at" TIMESTAMP NOT NULL,
    "updated_at" TIMESTAMP
);

ALTER TABLE "user"
    ADD CONSTRAINT "pk_user" PRIMARY KEY ("id"),
    ADD CONSTRAINT "uq_email" UNIQUE ("email");