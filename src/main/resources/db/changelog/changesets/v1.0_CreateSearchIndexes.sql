--liquibase formatted sql

--changeset carlos:2025_04_30_0
--comment: Create device table search indexes

create index device_id_name_index
    on public.device (name);

create index device_id_brand_index
    on public.device (brand);

