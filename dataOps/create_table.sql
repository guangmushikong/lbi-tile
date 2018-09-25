-- 日志表
CREATE TABLE public.t_log
(
  id bigserial NOT NULL PRIMARY KEY,
  ip text,
  message text,
  method text,
  usetime bigint,
  log_time timestamp with time zone NOT NULL DEFAULT now()
);


