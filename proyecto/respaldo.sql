--
-- PostgreSQL database dump
--

-- Dumped from database version 14.22 (Ubuntu 14.22-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 14.22 (Ubuntu 14.22-0ubuntu0.22.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: actuaciones; Type: TABLE; Schema: public; Owner: v
--

CREATE TABLE public.actuaciones (
    id integer NOT NULL,
    rol_id integer NOT NULL,
    usuario_id integer NOT NULL
);


ALTER TABLE public.actuaciones OWNER TO postgres;

--
-- Name: actuaciones_id_seq; Type: SEQUENCE; Schema: public; Owner: v
--

CREATE SEQUENCE public.actuaciones_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.actuaciones_id_seq OWNER TO postgres;

--
-- Name: actuaciones_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: v
--

ALTER SEQUENCE public.actuaciones_id_seq OWNED BY public.actuaciones.id;


--
-- Name: especialidades; Type: TABLE; Schema: public; Owner: v
--

CREATE TABLE public.especialidades (
    id integer NOT NULL,
    cocinero_id integer NOT NULL,
    plato_id integer NOT NULL
);


ALTER TABLE public.especialidades OWNER TO postgres;

--
-- Name: especialidades_id_seq; Type: SEQUENCE; Schema: public; Owner: v
--

CREATE SEQUENCE public.especialidades_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.especialidades_id_seq OWNER TO postgres;

--
-- Name: especialidades_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: v
--

ALTER SEQUENCE public.especialidades_id_seq OWNED BY public.especialidades.id;


--
-- Name: horarios; Type: TABLE; Schema: public; Owner: v
--

CREATE TABLE public.horarios (
    id integer NOT NULL,
    mesa_id integer NOT NULL,
    reservacion_id integer NOT NULL,
    inicio timestamp without time zone NOT NULL,
    duracion interval,
    CONSTRAINT horarios_duracion_check CHECK ((duracion < '02:00:00'::interval))
);


ALTER TABLE public.horarios OWNER TO postgres;

--
-- Name: horarios_id_seq; Type: SEQUENCE; Schema: public; Owner: v
--

CREATE SEQUENCE public.horarios_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.horarios_id_seq OWNER TO postgres;

--
-- Name: horarios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: v
--

ALTER SEQUENCE public.horarios_id_seq OWNED BY public.horarios.id;


--
-- Name: mesas; Type: TABLE; Schema: public; Owner: v
--

CREATE TABLE public.mesas (
    id integer NOT NULL,
    sillas integer NOT NULL
);


ALTER TABLE public.mesas OWNER TO postgres;

--
-- Name: mesas_id_seq; Type: SEQUENCE; Schema: public; Owner: v
--

CREATE SEQUENCE public.mesas_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.mesas_id_seq OWNER TO postgres;

--
-- Name: mesas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: v
--

ALTER SEQUENCE public.mesas_id_seq OWNED BY public.mesas.id;


--
-- Name: ordenes; Type: TABLE; Schema: public; Owner: v
--

CREATE TABLE public.ordenes (
    id integer NOT NULL,
    plato_id integer NOT NULL,
    pedido_id integer NOT NULL,
    estado integer NOT NULL,
    cantidad integer NOT NULL,
    solicitado timestamp without time zone NOT NULL,
    CONSTRAINT ordenes_cantidad_check CHECK ((cantidad > 0))
);


ALTER TABLE public.ordenes OWNER TO postgres;

--
-- Name: ordenes_id_seq; Type: SEQUENCE; Schema: public; Owner: v
--

CREATE SEQUENCE public.ordenes_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ordenes_id_seq OWNER TO postgres;

--
-- Name: ordenes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: v
--

ALTER SEQUENCE public.ordenes_id_seq OWNED BY public.ordenes.id;


--
-- Name: pedidos; Type: TABLE; Schema: public; Owner: v
--

CREATE TABLE public.pedidos (
    id integer NOT NULL,
    cliente_id integer NOT NULL,
    mesero_id integer NOT NULL
);


ALTER TABLE public.pedidos OWNER TO postgres;

--
-- Name: pedidos_id_seq; Type: SEQUENCE; Schema: public; Owner: v
--

CREATE SEQUENCE public.pedidos_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pedidos_id_seq OWNER TO postgres;

--
-- Name: pedidos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: v
--

ALTER SEQUENCE public.pedidos_id_seq OWNED BY public.pedidos.id;


--
-- Name: platos; Type: TABLE; Schema: public; Owner: v
--

CREATE TABLE public.platos (
    id integer NOT NULL,
    tipo_id integer NOT NULL,
    nombre text NOT NULL,
    descripcion text,
    tiempo interval NOT NULL,
    precio numeric(10,2) NOT NULL
);


ALTER TABLE public.platos OWNER TO postgres;

--
-- Name: platos_id_seq; Type: SEQUENCE; Schema: public; Owner: v
--

CREATE SEQUENCE public.platos_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.platos_id_seq OWNER TO postgres;

--
-- Name: platos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: v
--

ALTER SEQUENCE public.platos_id_seq OWNED BY public.platos.id;


--
-- Name: preparaciones; Type: TABLE; Schema: public; Owner: v
--

CREATE TABLE public.preparaciones (
    id integer NOT NULL,
    cocinero_id integer NOT NULL,
    orden_id integer NOT NULL
);


ALTER TABLE public.preparaciones OWNER TO postgres;

--
-- Name: preparaciones_id_seq; Type: SEQUENCE; Schema: public; Owner: v
--

CREATE SEQUENCE public.preparaciones_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.preparaciones_id_seq OWNER TO postgres;

--
-- Name: preparaciones_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: v
--

ALTER SEQUENCE public.preparaciones_id_seq OWNED BY public.preparaciones.id;


--
-- Name: reservaciones; Type: TABLE; Schema: public; Owner: v
--

CREATE TABLE public.reservaciones (
    id integer NOT NULL,
    cliente_id integer NOT NULL,
    cantidad integer NOT NULL,
    estado integer NOT NULL
);


ALTER TABLE public.reservaciones OWNER TO postgres;

--
-- Name: reservaciones_id_seq; Type: SEQUENCE; Schema: public; Owner: v
--

CREATE SEQUENCE public.reservaciones_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.reservaciones_id_seq OWNER TO postgres;

--
-- Name: reservaciones_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: v
--

ALTER SEQUENCE public.reservaciones_id_seq OWNED BY public.reservaciones.id;


--
-- Name: roles; Type: TABLE; Schema: public; Owner: v
--

CREATE TABLE public.roles (
    id integer NOT NULL,
    nombre text NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: v
--

CREATE SEQUENCE public.roles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.roles_id_seq OWNER TO postgres;

--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: v
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;


--
-- Name: tipos; Type: TABLE; Schema: public; Owner: v
--

CREATE TABLE public.tipos (
    id integer NOT NULL,
    nombre text NOT NULL
);


ALTER TABLE public.tipos OWNER TO postgres;

--
-- Name: tipos_id_seq; Type: SEQUENCE; Schema: public; Owner: v
--

CREATE SEQUENCE public.tipos_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tipos_id_seq OWNER TO postgres;

--
-- Name: tipos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: v
--

ALTER SEQUENCE public.tipos_id_seq OWNED BY public.tipos.id;


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: v
--

CREATE TABLE public.usuarios (
    id integer NOT NULL,
    nombre text NOT NULL,
    clave bytea NOT NULL,
    fecha_clave timestamp without time zone NOT NULL
);


ALTER TABLE public.usuarios OWNER TO postgres;

--
-- Name: usuarios_id_seq; Type: SEQUENCE; Schema: public; Owner: v
--

CREATE SEQUENCE public.usuarios_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.usuarios_id_seq OWNER TO postgres;

--
-- Name: usuarios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: v
--

ALTER SEQUENCE public.usuarios_id_seq OWNED BY public.usuarios.id;


--
-- Name: actuaciones id; Type: DEFAULT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.actuaciones ALTER COLUMN id SET DEFAULT nextval('public.actuaciones_id_seq'::regclass);


--
-- Name: especialidades id; Type: DEFAULT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.especialidades ALTER COLUMN id SET DEFAULT nextval('public.especialidades_id_seq'::regclass);


--
-- Name: horarios id; Type: DEFAULT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.horarios ALTER COLUMN id SET DEFAULT nextval('public.horarios_id_seq'::regclass);


--
-- Name: mesas id; Type: DEFAULT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.mesas ALTER COLUMN id SET DEFAULT nextval('public.mesas_id_seq'::regclass);


--
-- Name: ordenes id; Type: DEFAULT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.ordenes ALTER COLUMN id SET DEFAULT nextval('public.ordenes_id_seq'::regclass);


--
-- Name: pedidos id; Type: DEFAULT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.pedidos ALTER COLUMN id SET DEFAULT nextval('public.pedidos_id_seq'::regclass);


--
-- Name: platos id; Type: DEFAULT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.platos ALTER COLUMN id SET DEFAULT nextval('public.platos_id_seq'::regclass);


--
-- Name: preparaciones id; Type: DEFAULT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.preparaciones ALTER COLUMN id SET DEFAULT nextval('public.preparaciones_id_seq'::regclass);


--
-- Name: reservaciones id; Type: DEFAULT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.reservaciones ALTER COLUMN id SET DEFAULT nextval('public.reservaciones_id_seq'::regclass);


--
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- Name: tipos id; Type: DEFAULT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.tipos ALTER COLUMN id SET DEFAULT nextval('public.tipos_id_seq'::regclass);


--
-- Name: usuarios id; Type: DEFAULT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.usuarios ALTER COLUMN id SET DEFAULT nextval('public.usuarios_id_seq'::regclass);


--
-- Data for Name: actuaciones; Type: TABLE DATA; Schema: public; Owner: v
--

COPY public.actuaciones (id, rol_id, usuario_id) FROM stdin;
1	1	43
2	1	44
4	1	46
7	1	49
3	3	45
9	2	33
10	5	35
11	5	25
12	4	1
13	4	30
14	4	38
15	4	21
16	4	24
17	4	28
18	4	26
19	4	9
21	4	32
22	4	34
23	4	15
24	4	22
25	4	2
26	4	16
27	4	4
28	4	5
29	4	27
30	4	31
31	4	12
32	4	6
33	4	3
34	4	29
35	4	13
36	4	17
37	4	8
38	4	40
39	4	39
40	4	19
41	4	23
42	4	36
43	3	10
44	4	14
45	4	37
5	2	47
20	3	51
46	5	52
6	3	48
\.


--
-- Data for Name: especialidades; Type: TABLE DATA; Schema: public; Owner: v
--

COPY public.especialidades (id, cocinero_id, plato_id) FROM stdin;
\.


--
-- Data for Name: horarios; Type: TABLE DATA; Schema: public; Owner: v
--

COPY public.horarios (id, mesa_id, reservacion_id, inicio, duracion) FROM stdin;
2	1	22	2026-04-13 12:10:00	01:59:00
3	4	23	2026-04-13 14:30:00	01:59:00
4	6	24	2026-04-13 13:30:00	01:59:00
5	7	25	2026-04-13 15:30:00	01:59:00
6	2	26	2026-04-13 12:00:00	01:59:00
7	9	27	2026-04-13 13:45:00	01:59:00
\.


--
-- Data for Name: mesas; Type: TABLE DATA; Schema: public; Owner: v
--

COPY public.mesas (id, sillas) FROM stdin;
2	4
3	4
4	4
5	6
6	2
7	4
8	6
9	6
10	4
1	4
11	5
\.


--
-- Data for Name: ordenes; Type: TABLE DATA; Schema: public; Owner: v
--

COPY public.ordenes (id, plato_id, pedido_id, estado, cantidad, solicitado) FROM stdin;
46	8	14	3	2	2026-02-16 07:07:26.197166
45	3	13	3	1	2026-02-16 07:07:26.196143
44	4	13	3	1	2026-02-16 07:07:26.1957
43	3	13	3	2	2026-02-16 07:07:26.195202
42	4	12	3	1	2026-02-16 07:07:26.194291
41	23	12	3	2	2026-02-16 07:07:26.19375
53	2	17	3	3	2026-04-13 12:39:30.855913
48	12	14	3	1	2026-02-16 07:07:26.19822
27	19	8	3	2	2026-02-16 07:07:26.181617
15	19	4	3	2	2026-02-16 07:07:26.170731
23	17	7	3	2	2026-02-16 07:07:26.179049
21	18	6	3	1	2026-02-16 07:07:26.177565
40	17	12	3	2	2026-02-16 07:07:26.193216
39	17	11	3	1	2026-02-16 07:07:26.18932
38	5	11	3	1	2026-02-16 07:07:26.188798
37	7	11	3	2	2026-02-16 07:07:26.188147
36	18	11	3	1	2026-02-16 07:07:26.187559
35	14	10	3	1	2026-02-16 07:07:26.18646
54	2	18	3	3	2026-04-13 12:40:14.470326
34	22	10	3	1	2026-02-16 07:07:26.185983
52	1	16	3	2	2026-04-13 12:39:21.526348
33	4	10	3	1	2026-02-16 07:07:26.185444
51	1	15	3	1	2026-02-16 07:07:26.202129
50	10	15	3	1	2026-02-16 07:07:26.201652
32	2	10	3	1	2026-02-16 07:07:26.185009
49	29	14	3	2	2026-02-16 07:07:26.1989
47	19	14	3	2	2026-02-16 07:07:26.197692
31	7	9	3	1	2026-02-16 07:07:26.184104
30	25	9	3	2	2026-02-16 07:07:26.183669
29	5	9	3	2	2026-02-16 07:07:26.183135
28	23	8	3	1	2026-02-16 07:07:26.182132
26	26	7	3	1	2026-02-16 07:07:26.180664
25	30	7	3	1	2026-02-16 07:07:26.180173
24	16	7	3	1	2026-02-16 07:07:26.17959
22	18	6	3	2	2026-02-16 07:07:26.178103
20	15	6	3	1	2026-02-16 07:07:26.176941
19	29	5	3	1	2026-02-16 07:07:26.17584
18	1	5	3	2	2026-02-16 07:07:26.175249
17	2	5	3	2	2026-02-16 07:07:26.173823
16	25	5	3	1	2026-02-16 07:07:26.173192
14	16	4	3	1	2026-02-16 07:07:26.169647
13	28	4	3	1	2026-02-16 07:07:26.169089
12	14	4	3	2	2026-02-16 07:07:26.168472
11	25	3	3	1	2026-02-16 07:07:26.166659
10	26	3	3	1	2026-02-16 07:07:26.164655
9	1	3	3	1	2026-02-16 07:07:26.163736
8	29	3	3	2	2026-02-16 07:07:26.162877
7	17	2	3	2	2026-02-16 07:07:26.159663
6	22	2	3	1	2026-02-16 07:07:26.158666
5	29	2	3	2	2026-02-16 07:07:26.158124
4	15	1	3	2	2026-02-16 07:07:26.157016
3	15	1	3	2	2026-02-16 07:07:26.156343
2	26	1	3	1	2026-02-16 07:07:26.155761
1	6	1	3	2	2026-02-16 07:07:26.154249
55	2	19	1	1	2026-04-13 12:50:09.698514
56	27	20	1	1	2026-04-13 12:50:24.098106
57	7	21	1	1	2026-04-13 12:50:33.805082
58	24	22	1	1	2026-04-13 13:00:16.169802
59	30	23	1	1	2026-04-13 13:00:22.901657
60	5	24	1	2	2026-04-13 13:00:30.079619
61	25	25	1	1	2026-04-13 13:00:41.217077
62	6	26	1	3	2026-04-13 13:00:49.621409
63	28	27	1	2	2026-04-13 13:01:08.974219
64	18	28	1	3	2026-04-13 13:01:19.664366
65	22	29	1	1	2026-04-13 13:49:00.883693
66	10	30	1	1	2026-04-13 13:49:12.489344
67	15	31	1	1	2026-04-13 13:49:34.161863
\.


--
-- Data for Name: pedidos; Type: TABLE DATA; Schema: public; Owner: v
--

COPY public.pedidos (id, cliente_id, mesero_id) FROM stdin;
1	30	5
2	23	2
3	32	2
4	23	5
5	32	3
6	40	4
7	25	2
8	23	2
9	37	1
10	22	2
11	28	4
12	31	4
13	21	5
14	26	2
15	26	3
16	1	47
17	1	47
18	1	47
19	26	47
20	1	47
21	1	47
22	1	47
23	1	47
24	1	47
25	26	47
26	26	47
27	26	47
28	26	47
29	4	47
30	4	47
31	4	47
\.


--
-- Data for Name: platos; Type: TABLE DATA; Schema: public; Owner: v
--

COPY public.platos (id, tipo_id, nombre, descripcion, tiempo, precio) FROM stdin;
1	1	Carpaccio de Res	Láminas de solomillo con alcaparras	00:12:00	14.50 
2	1	Bruschetta Toscana	Pan tostado con tomate y albahaca	00:08:00	9.00  
3	1	Ceviche de Corvina	Marinado en leche de tigre	00:15:00	16.00 
4	1	Empanadas de Carne	Rellenas de carne cortada a cuchillo	00:10:00	8.50 
5	1	Sopa de Cebolla	Gratinada con queso Gruyère	00:20:00	11.00 
6	1	Calamares Crujientes	Aros de calamar con salsa tártara	00:12:00	13.00 
7	1	Nachos Supremos	Con guacamole, queso y jalapeños	00:10:00	12.00 
8	1	Provoleta Asada	Queso fundido con orégano y aceite	00:15:00	10.50 
9	1	Gazpacho Andaluz	Sopa fría de tomate y hortalizas	00:05:00	8.00 
10	1	Tartar de Salmón	Con aguacate y un toque de lima	00:14:00	17.00 
11	2	Lomo Saltado	Salteado con cebolla y papas fritas	00:25:00	22.00 
12	2	Risotto de Hongos	Arroz cremoso con setas silvestres	00:30:00	19.50 
13	2	Salmón al Eneldo	A la plancha con puré de papa	00:22:00	24.00 
14	2	Pasta Carbonara	Con guanciale y queso pecorino	00:18:00	17.00 
15	2	Hamburguesa Gourmet	Carne angus con queso brie	00:15:00	16.50
16	2	Entrecot a la Parrilla	300g con mantequilla de hierbas	00:25:00	28.00
17	2	Lasagna de Carne	Boloñesa y bechamel casera	00:25:00	16.00
18	2	Paella de Marisco	Arroz con langostinos y mejillones	00:45:00	32.00
19	2	Cochinillo Asado	Cocinado al horno de leña	00:50:00	40.00
20	2	Atún Sellado	Con costra de sésamo y salsa ponzu	00:20:00	29.00
21	3	Limonada Natural	Refrescante limonada frapeada	00:05:00	4.50
22	3	Vino Tinto Reserva	Copa de Malbec de la casa	00:02:00	8.00
23	3	Cerveza Artesanal	IPA elaborada localmente	00:03:00	6.00
24	3	Sangría Tradicional	Vino tinto y frutas frescas	00:07:00	12.00
25	3	Café Espresso	Grano de altura tostado	00:04:00	3.00
26	3	Smoothie de Mango	Mango natural y yogur	00:06:00	5.50
27	3	Té Helado Melocotón	Infusión fría con fruta natural	00:03:00	4.00
28	3	Cóctel Margarita	Tequila, triple sec y limón	00:07:00	11.00
29	3	Jugo de Naranja	Recién exprimido	00:05:00	5.00
30	3	Agua Mineral	Con o sin gas (500ml)	00:01:00	2.50
\.


--
-- Data for Name: preparaciones; Type: TABLE DATA; Schema: public; Owner: v
--

COPY public.preparaciones (id, cocinero_id, orden_id) FROM stdin;
\.


--
-- Data for Name: reservaciones; Type: TABLE DATA; Schema: public; Owner: v
--

COPY public.reservaciones (id, cliente_id, cantidad, estado) FROM stdin;
1	35	1	3
2	25	6	0
3	30	8	3
4	28	5	0
5	26	2	2
6	25	3	3
7	38	4	3
8	36	4	2
9	33	3	0
10	21	6	2
11	21	2	2
12	34	8	2
13	21	5	1
14	40	4	2
15	32	1	2
16	26	5	1
17	30	6	2
18	28	3	2
19	40	6	1
20	29	3	3
23	32	3	1
25	36	4	1
26	26	3	2
22	1	3	2
24	4	1	2
27	5	5	2
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: v
--

COPY public.roles (id, nombre) FROM stdin;
1	Administrador
2	Mesero
3	Cocinero
4	Cliente
5	Maitre
\.


--
-- Data for Name: tipos; Type: TABLE DATA; Schema: public; Owner: v
--

COPY public.tipos (id, nombre) FROM stdin;
1	Entrada
2	Plato Fuerte
3	Bebida
\.


--
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: v
--

COPY public.usuarios (id, nombre, clave, fecha_clave) FROM stdin;
43	Victor	\\x243279243130247975466e7352427562536a42362e576c46763171674f564e36466f6858742f5643714354363643506b54386c6c65774c36676c4965	2026-03-08 21:00:57.388785
44	Adam	\\x24327924313024655773354536756b5767475169323169354f2f756b2e4845656e66754c6538516939454731666c2f47303673432e396f7945766679	2026-03-08 21:04:25.303507
46	John	\\x2432792431302444783969775337764279464256463952374b36455465764c3954516e7148416d465a764a34454d69753466445a3135617776796853	2026-03-08 21:18:53.337872
49	prueba3	\\x243279243130247754736f7874556b4169395752446365306c5866617573626262774350484141646876536c4a706361734f6b3934556a674f697869	2026-04-11 21:00:15.430931
5	Gil Espejo Suárez	\\x5c7861613964643635373262633837653930	2025-06-21 15:11:55.233726
27	Jacinto Belmonte Grau	\\x5c7865316363353131626661396465306364	2025-11-16 03:35:45.854693
31	Jenny Moliner Martínez	\\x5c7865326261396565343836666362343363	2025-08-24 04:57:08.995474
12	Juan Francisco Vargas Ugarte	\\x5c7831373463653563613165396439333632	2025-03-07 22:09:58.510123
6	Juanita Díez	\\x5c7861313361383663363365323566336339	2025-09-25 00:57:09.852545
3	Reina Salinas Páez	\\x5c7836663264363962386436616663313264	2025-12-28 05:02:16.892075
29	Régulo Gallart-Mayo	\\x5c7830316263323766646161333264373333	2025-06-02 05:44:44.754275
45	Adam	\\x24327924313024434c6d3357466f6d4641484c496c4d525a5667444565363452586136746b312e766c3676724f5151524a70444f6c68365230766c32	2026-03-08 21:12:58.636232
13	Paulina del Mendizábal	\\x5c7862616661663531346530343134306239	2026-02-01 19:48:13.180024
33	Álvaro Bertrán Aramburu	\\x243279243130245a574674495165387a613430686b304d4373446357654d6a5773595347314a444267566276584232426f47476d336b6c59642f4b53	2026-04-12 22:17:09.28396
35	Aurelio del Parra	\\x5c7839663530646239643064633066623338	2025-09-30 10:33:44.854828
25	Benigno Villalonga Pomares	\\x5c7866643936633966393730343139626135	2025-10-28 21:13:36.545422
1	Casemiro del Blanca	\\x5c7834303361393765613066326561306138	2025-11-12 05:45:14.87348
30	Celestina de Guitart	\\x5c7864386563623234343161343937316164	2025-08-07 00:57:35.228221
38	Rico Valero	\\x5c7834623762333538346339323936303264	2025-08-31 08:09:29.789048
21	Ciriaco Morán Bilbao	\\x5c7830316532323833666335353430376237	2025-12-08 00:52:39.723382
24	Cosme Arellano	\\x5c7831336266623935323830323038633532	2025-12-01 04:33:13.803265
28	Custodia Dominga Canals Santamaria	\\x5c7838326130386132643232333664323137	2026-01-28 22:05:19.572405
26	Darío de Robledo	\\x5c7865653265376263663639643735383833	2025-06-06 17:19:08.766543
9	Emiliano Castell Carreras	\\x5c7839376561623130303263616633643034	2025-12-06 00:51:26.182358
32	Evita Saldaña Garrido	\\x5c7832323266626538306264353734623733	2025-09-26 00:12:46.849386
34	Federico Sales Roda	\\x5c7866346639343166376438346639616563	2025-05-29 04:27:15.111607
15	Hilda Losa Esparza	\\x5c7838373931616637346332666162663636	2025-08-20 19:17:12.604475
22	Felicidad Luján Colom	\\x5c7832386532373362336233663335653638	2025-09-17 06:38:42.371523
2	Marino de Valbuena	\\x5c7839646363353630323964333632373061	2025-09-21 21:20:27.08294
16	Filomena Carlos Benitez	\\x5c7835353666376561353636626333303862	2025-04-28 01:34:15.693973
4	Florinda Ortuño Bermejo	\\x5c7838353266336237343162393565396330	2025-12-05 01:48:58.235187
17	Paula de Elorza	\\x5c7863636662613638333437626163326135	2026-01-15 03:31:23.224509
8	Marciano Gelabert	\\x5c7863623961326331616363336434323833	2025-11-29 04:47:41.721446
40	Melania del Bejarano	\\x5c7834386139346636636435616433623365	2025-07-05 09:35:37.090402
39	Olalla de Haro	\\x5c7863316663363538626338383538313730	2025-07-25 13:58:22.997032
19	Lucila de Melero	\\x5c7863313136343538323339306664363235	2025-11-22 10:43:18.236712
23	Luis Cobos	\\x5c7861653230643664666332376639623865	2025-04-14 10:54:29.190958
36	Magdalena Hurtado Hervia	\\x5c7861326431613530393662653563626633	2025-08-15 11:43:01.291004
10	Manu Jover Múñiz	\\x5c7839333464303066353838386238346535	2025-05-12 14:10:40.629886
14	Nacho Gaya Carbonell	\\x5c7839613533653264626163356335643166	2025-05-12 16:06:11.232272
37	Octavia Salamanca Manso	\\x5c7830303537383839653462333639366531	2025-09-03 17:27:08.925801
47	prueba	\\x243279243130247a69304f62646452733774664864636c445530454b656d69556a5a4c322f57492e4f505869734e6e574150357a314759655a694269	2026-04-13 11:59:06.821364
51	prueba5	\\x2432792431302437724a476d475636316b51635369746c463062612e2e7a73336f4a70645357395837627633476e6735706352676f42366d4f706836	2026-04-13 11:59:27.608363
52	prueba6	\\x243279243130242f6e4165536e626e4463476e737342356f2f6951694f68364f42462e6a4278577a7761445136686b7a514f6e59414c46745151634b	2026-04-13 11:59:49.345261
48	prueba2	\\x24327924313024486b3576435278316455756476495a6e4d384355522e543476697037516375632f44356e2f38494758687a43584b434544536c4361	2026-04-13 12:40:40.225537
\.


--
-- Name: actuaciones_id_seq; Type: SEQUENCE SET; Schema: public; Owner: v
--

SELECT pg_catalog.setval('public.actuaciones_id_seq', 46, true);


--
-- Name: especialidades_id_seq; Type: SEQUENCE SET; Schema: public; Owner: v
--

SELECT pg_catalog.setval('public.especialidades_id_seq', 1, false);


--
-- Name: horarios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: v
--

SELECT pg_catalog.setval('public.horarios_id_seq', 7, true);


--
-- Name: mesas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: v
--

SELECT pg_catalog.setval('public.mesas_id_seq', 11, true);


--
-- Name: ordenes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: v
--

SELECT pg_catalog.setval('public.ordenes_id_seq', 67, true);


--
-- Name: pedidos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: v
--

SELECT pg_catalog.setval('public.pedidos_id_seq', 31, true);


--
-- Name: platos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: v
--

SELECT pg_catalog.setval(
  'public.platos_id_seq',
  COALESCE((SELECT MAX(id) FROM public.platos), 0),
  true
);


--
-- Name: preparaciones_id_seq; Type: SEQUENCE SET; Schema: public; Owner: v
--

SELECT pg_catalog.setval('public.preparaciones_id_seq', 1, false);


--
-- Name: reservaciones_id_seq; Type: SEQUENCE SET; Schema: public; Owner: v
--

SELECT pg_catalog.setval('public.reservaciones_id_seq', 27, true);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: v
--

SELECT pg_catalog.setval('public.roles_id_seq', 5, true);


--
-- Name: tipos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: v
--

SELECT pg_catalog.setval(
  'public.tipos_id_seq',
  COALESCE((SELECT MAX(id) FROM public.tipos), 0),
  true
);


--
-- Name: usuarios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: v
--

SELECT pg_catalog.setval('public.usuarios_id_seq', 52, true);


--
-- Name: actuaciones actuaciones_pkey; Type: CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.actuaciones
    ADD CONSTRAINT actuaciones_pkey PRIMARY KEY (id);


--
-- Name: especialidades especialidades_pkey; Type: CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.especialidades
    ADD CONSTRAINT especialidades_pkey PRIMARY KEY (id);


--
-- Name: horarios horarios_pkey; Type: CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.horarios
    ADD CONSTRAINT horarios_pkey PRIMARY KEY (id);


--
-- Name: mesas mesas_pkey; Type: CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.mesas
    ADD CONSTRAINT mesas_pkey PRIMARY KEY (id);


--
-- Name: ordenes ordenes_pkey; Type: CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.ordenes
    ADD CONSTRAINT ordenes_pkey PRIMARY KEY (id);


--
-- Name: pedidos pedidos_pkey; Type: CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.pedidos
    ADD CONSTRAINT pedidos_pkey PRIMARY KEY (id);


--
-- Name: platos platos_pkey; Type: CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.platos
    ADD CONSTRAINT platos_pkey PRIMARY KEY (id);


--
-- Name: preparaciones preparaciones_pkey; Type: CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.preparaciones
    ADD CONSTRAINT preparaciones_pkey PRIMARY KEY (id);


--
-- Name: reservaciones reservaciones_pkey; Type: CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.reservaciones
    ADD CONSTRAINT reservaciones_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: tipos tipos_pkey; Type: CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.tipos
    ADD CONSTRAINT tipos_pkey PRIMARY KEY (id);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- Name: pedidos fk_cliente_id; Type: FK CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.pedidos
    ADD CONSTRAINT fk_cliente_id FOREIGN KEY (cliente_id) REFERENCES public.usuarios(id);


--
-- Name: reservaciones fk_cliente_id; Type: FK CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.reservaciones
    ADD CONSTRAINT fk_cliente_id FOREIGN KEY (cliente_id) REFERENCES public.usuarios(id);


--
-- Name: especialidades fk_cocinero_id; Type: FK CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.especialidades
    ADD CONSTRAINT fk_cocinero_id FOREIGN KEY (cocinero_id) REFERENCES public.usuarios(id);


--
-- Name: preparaciones fk_cocinero_id; Type: FK CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.preparaciones
    ADD CONSTRAINT fk_cocinero_id FOREIGN KEY (cocinero_id) REFERENCES public.usuarios(id);


--
-- Name: horarios fk_mesa_id; Type: FK CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.horarios
    ADD CONSTRAINT fk_mesa_id FOREIGN KEY (mesa_id) REFERENCES public.mesas(id);


--
-- Name: pedidos fk_mesero_id; Type: FK CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.pedidos
    ADD CONSTRAINT fk_mesero_id FOREIGN KEY (mesero_id) REFERENCES public.usuarios(id);


--
-- Name: preparaciones fk_orden_id; Type: FK CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.preparaciones
    ADD CONSTRAINT fk_orden_id FOREIGN KEY (orden_id) REFERENCES public.ordenes(id);


--
-- Name: ordenes fk_pedido_id; Type: FK CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.ordenes
    ADD CONSTRAINT fk_pedido_id FOREIGN KEY (pedido_id) REFERENCES public.pedidos(id);


--
-- Name: especialidades fk_plato_id; Type: FK CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.especialidades
    ADD CONSTRAINT fk_plato_id FOREIGN KEY (plato_id) REFERENCES public.platos(id);


--
-- Name: ordenes fk_plato_id; Type: FK CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.ordenes
    ADD CONSTRAINT fk_plato_id FOREIGN KEY (plato_id) REFERENCES public.platos(id);


--
-- Name: horarios fk_reservacion_id; Type: FK CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.horarios
    ADD CONSTRAINT fk_reservacion_id FOREIGN KEY (reservacion_id) REFERENCES public.reservaciones(id);


--
-- Name: actuaciones fk_rol_id; Type: FK CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.actuaciones
    ADD CONSTRAINT fk_rol_id FOREIGN KEY (rol_id) REFERENCES public.roles(id);


--
-- Name: platos fk_tipo_id; Type: FK CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.platos
    ADD CONSTRAINT fk_tipo_id FOREIGN KEY (tipo_id) REFERENCES public.tipos(id);


--
-- Name: actuaciones fk_usuario_id; Type: FK CONSTRAINT; Schema: public; Owner: v
--

ALTER TABLE ONLY public.actuaciones
    ADD CONSTRAINT fk_usuario_id FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- Requisitos adicionales del PDF: tablespaces, particiones y triggers
--
-- La restauracion debe ejecutarse con un usuario con privilegios para
-- CREATE TABLESPACE.
--
-- Estas carpetas se crean desde psql para el entorno Windows/XAMPP usado
-- en la entrega.

\! if not exist "C:\xampp\htdocs\proyecto\pg_tablespaces\restaurante_ts_reservas" mkdir "C:\xampp\htdocs\proyecto\pg_tablespaces\restaurante_ts_reservas"
\! if not exist "C:\xampp\htdocs\proyecto\pg_tablespaces\restaurante_ts_historico" mkdir "C:\xampp\htdocs\proyecto\pg_tablespaces\restaurante_ts_historico"

SELECT 'CREATE TABLESPACE restaurante_ts_reservas OWNER postgres LOCATION ''C:/xampp/htdocs/proyecto/pg_tablespaces/restaurante_ts_reservas'''
WHERE NOT EXISTS (SELECT 1 FROM pg_tablespace WHERE spcname = 'restaurante_ts_reservas') \gexec

SELECT 'CREATE TABLESPACE restaurante_ts_historico OWNER postgres LOCATION ''C:/xampp/htdocs/proyecto/pg_tablespaces/restaurante_ts_historico'''
WHERE NOT EXISTS (SELECT 1 FROM pg_tablespace WHERE spcname = 'restaurante_ts_historico') \gexec

UPDATE public.reservaciones
SET estado = 4
WHERE estado = 0;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_partitioned_table
        WHERE partrelid = 'public.horarios'::regclass
    ) THEN
        EXECUTE 'LOCK TABLE public.horarios IN ACCESS EXCLUSIVE MODE';
        EXECUTE 'ALTER TABLE public.horarios RENAME TO horarios_sin_particionar';
        EXECUTE 'ALTER SEQUENCE public.horarios_id_seq OWNED BY NONE';

        EXECUTE 'CREATE TABLE public.horarios (
            id integer NOT NULL DEFAULT nextval(''public.horarios_id_seq''::regclass),
            mesa_id integer NOT NULL,
            reservacion_id integer NOT NULL,
            inicio timestamp without time zone NOT NULL,
            duracion interval,
            CONSTRAINT horarios_duracion_check CHECK (duracion > interval ''0 minutes'' AND duracion <= interval ''02:00:00'')
        ) PARTITION BY RANGE (inicio)';

        EXECUTE 'CREATE TABLE public.horarios_2025 PARTITION OF public.horarios
            FOR VALUES FROM (''2025-01-01'') TO (''2026-01-01'')
            TABLESPACE restaurante_ts_historico';

        EXECUTE 'CREATE TABLE public.horarios_2026 PARTITION OF public.horarios
            FOR VALUES FROM (''2026-01-01'') TO (''2027-01-01'')
            TABLESPACE restaurante_ts_reservas';

        EXECUTE 'CREATE TABLE public.horarios_2027 PARTITION OF public.horarios
            FOR VALUES FROM (''2027-01-01'') TO (''2028-01-01'')
            TABLESPACE restaurante_ts_reservas';

        EXECUTE 'CREATE TABLE public.horarios_default PARTITION OF public.horarios
            DEFAULT
            TABLESPACE restaurante_ts_reservas';

        EXECUTE 'INSERT INTO public.horarios (id, mesa_id, reservacion_id, inicio, duracion)
            SELECT id, mesa_id, reservacion_id, inicio, duracion
            FROM public.horarios_sin_particionar';

        EXECUTE 'DROP TABLE public.horarios_sin_particionar';
        EXECUTE 'ALTER SEQUENCE public.horarios_id_seq OWNED BY public.horarios.id';
    END IF;
END
$$;

ALTER TABLE public.horarios DROP CONSTRAINT IF EXISTS horarios_pkey;
ALTER TABLE public.horarios ADD CONSTRAINT horarios_pkey PRIMARY KEY (id, inicio);

ALTER TABLE public.horarios DROP CONSTRAINT IF EXISTS fk_mesa_id;
ALTER TABLE public.horarios
    ADD CONSTRAINT fk_mesa_id FOREIGN KEY (mesa_id) REFERENCES public.mesas(id);

ALTER TABLE public.horarios DROP CONSTRAINT IF EXISTS fk_reservacion_id;
ALTER TABLE public.horarios
    ADD CONSTRAINT fk_reservacion_id FOREIGN KEY (reservacion_id) REFERENCES public.reservaciones(id);

ALTER TABLE public.mesas DROP CONSTRAINT IF EXISTS mesas_sillas_check;
ALTER TABLE public.mesas
    ADD CONSTRAINT mesas_sillas_check CHECK (sillas > 0);

ALTER TABLE public.platos DROP CONSTRAINT IF EXISTS platos_precio_check;
ALTER TABLE public.platos
    ADD CONSTRAINT platos_precio_check CHECK (precio >= 0);

ALTER TABLE public.reservaciones DROP CONSTRAINT IF EXISTS reservaciones_cantidad_check;
ALTER TABLE public.reservaciones
    ADD CONSTRAINT reservaciones_cantidad_check CHECK (cantidad > 0);

ALTER TABLE public.reservaciones DROP CONSTRAINT IF EXISTS reservaciones_estado_check;
ALTER TABLE public.reservaciones
    ADD CONSTRAINT reservaciones_estado_check CHECK (estado IN (1, 2, 3, 4));

ALTER TABLE public.ordenes DROP CONSTRAINT IF EXISTS ordenes_estado_check;
ALTER TABLE public.ordenes
    ADD CONSTRAINT ordenes_estado_check CHECK (estado IN (1, 2, 3));

ALTER TABLE public.actuaciones DROP CONSTRAINT IF EXISTS actuaciones_usuario_unico;
ALTER TABLE public.actuaciones
    ADD CONSTRAINT actuaciones_usuario_unico UNIQUE (usuario_id);

ALTER TABLE public.especialidades DROP CONSTRAINT IF EXISTS especialidades_unicas;
ALTER TABLE public.especialidades
    ADD CONSTRAINT especialidades_unicas UNIQUE (cocinero_id, plato_id);

ALTER TABLE public.preparaciones DROP CONSTRAINT IF EXISTS preparaciones_orden_unica;
ALTER TABLE public.preparaciones
    ADD CONSTRAINT preparaciones_orden_unica UNIQUE (orden_id);

CREATE OR REPLACE FUNCTION public.usuario_tiene_rol(p_usuario_id integer, p_rol text)
RETURNS boolean AS $$
BEGIN
    RETURN EXISTS (
        SELECT 1
        FROM public.actuaciones a
        JOIN public.roles r ON r.id = a.rol_id
        WHERE a.usuario_id = p_usuario_id
          AND r.nombre ILIKE p_rol
    );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.validar_horario_reserva()
RETURNS trigger AS $$
DECLARE
    v_cantidad integer;
    v_estado integer;
    v_sillas integer;
    v_cupo_total integer;
    v_ocupadas integer;
BEGIN
    SELECT r.cantidad, r.estado, m.sillas
    INTO v_cantidad, v_estado, v_sillas
    FROM public.reservaciones r
    JOIN public.mesas m ON m.id = NEW.mesa_id
    WHERE r.id = NEW.reservacion_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'La reservacion o la mesa no existe';
    END IF;

    IF NEW.duracion IS NULL OR NEW.duracion <= interval '0 minutes' OR NEW.duracion > interval '02:00:00' THEN
        RAISE EXCEPTION 'La duracion de la reserva debe estar entre 1 minuto y 2 horas';
    END IF;

    IF v_estado IN (1, 2) THEN
        IF v_cantidad > v_sillas THEN
            RAISE EXCEPTION 'La cantidad de personas (%) supera las sillas de la mesa (%)', v_cantidad, v_sillas;
        END IF;

        IF EXISTS (
            SELECT 1
            FROM public.horarios h
            JOIN public.reservaciones r ON r.id = h.reservacion_id
            WHERE h.mesa_id = NEW.mesa_id
              AND h.reservacion_id <> NEW.reservacion_id
              AND r.estado IN (1, 2)
              AND (h.inicio, h.inicio + h.duracion)
                  OVERLAPS (NEW.inicio, NEW.inicio + NEW.duracion)
        ) THEN
            RAISE EXCEPTION 'Cruce de reservas para la mesa %', NEW.mesa_id;
        END IF;

        SELECT COALESCE(SUM(r.cantidad), 0)
        INTO v_ocupadas
        FROM public.horarios h
        JOIN public.reservaciones r ON r.id = h.reservacion_id
        WHERE h.reservacion_id <> NEW.reservacion_id
          AND r.estado IN (1, 2)
          AND (h.inicio, h.inicio + h.duracion)
              OVERLAPS (NEW.inicio, NEW.inicio + NEW.duracion);

        SELECT COALESCE(SUM(sillas), 0)
        INTO v_cupo_total
        FROM public.mesas;

        IF v_ocupadas + v_cantidad > v_cupo_total THEN
            RAISE EXCEPTION 'Se supera el cupo total del restaurante para ese horario';
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.validar_reservacion()
RETURNS trigger AS $$
DECLARE
    v_horario record;
    v_cupo_total integer;
    v_ocupadas integer;
BEGIN
    IF NEW.cantidad <= 0 THEN
        RAISE EXCEPTION 'La cantidad de personas debe ser mayor a cero';
    END IF;

    IF NOT public.usuario_tiene_rol(NEW.cliente_id, 'Cliente') THEN
        RAISE EXCEPTION 'El usuario % no tiene rol Cliente', NEW.cliente_id;
    END IF;

    IF NEW.estado IN (1, 2) THEN
        SELECT COALESCE(SUM(sillas), 0)
        INTO v_cupo_total
        FROM public.mesas;

        FOR v_horario IN
            SELECT h.inicio, h.duracion, h.mesa_id, m.sillas
            FROM public.horarios h
            JOIN public.mesas m ON m.id = h.mesa_id
            WHERE h.reservacion_id = NEW.id
        LOOP
            IF NEW.cantidad > v_horario.sillas THEN
                RAISE EXCEPTION 'La cantidad de personas (%) supera las sillas de la mesa (%)', NEW.cantidad, v_horario.sillas;
            END IF;

            SELECT COALESCE(SUM(r.cantidad), 0)
            INTO v_ocupadas
            FROM public.horarios h
            JOIN public.reservaciones r ON r.id = h.reservacion_id
            WHERE h.reservacion_id <> NEW.id
              AND r.estado IN (1, 2)
              AND (h.inicio, h.inicio + h.duracion)
                  OVERLAPS (v_horario.inicio, v_horario.inicio + v_horario.duracion);

            IF v_ocupadas + NEW.cantidad > v_cupo_total THEN
                RAISE EXCEPTION 'Se supera el cupo total del restaurante para ese horario';
            END IF;
        END LOOP;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.validar_pedido_roles()
RETURNS trigger AS $$
BEGIN
    IF NOT public.usuario_tiene_rol(NEW.cliente_id, 'Cliente') THEN
        RAISE EXCEPTION 'El usuario % no tiene rol Cliente', NEW.cliente_id;
    END IF;

    IF NOT public.usuario_tiene_rol(NEW.mesero_id, 'Mesero') THEN
        RAISE EXCEPTION 'El usuario % no tiene rol Mesero', NEW.mesero_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.validar_especialidad_cocinero()
RETURNS trigger AS $$
BEGIN
    IF NOT public.usuario_tiene_rol(NEW.cocinero_id, 'Cocinero') THEN
        RAISE EXCEPTION 'El usuario % no tiene rol Cocinero', NEW.cocinero_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.validar_preparacion_especialidad()
RETURNS trigger AS $$
DECLARE
    v_plato_id integer;
BEGIN
    IF NOT public.usuario_tiene_rol(NEW.cocinero_id, 'Cocinero') THEN
        RAISE EXCEPTION 'El usuario % no tiene rol Cocinero', NEW.cocinero_id;
    END IF;

    SELECT plato_id
    INTO v_plato_id
    FROM public.ordenes
    WHERE id = NEW.orden_id;

    IF v_plato_id IS NULL THEN
        RAISE EXCEPTION 'La orden % no existe', NEW.orden_id;
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM public.especialidades e
        WHERE e.cocinero_id = NEW.cocinero_id
          AND e.plato_id = v_plato_id
    ) THEN
        RAISE EXCEPTION 'El cocinero % no tiene especialidad para el plato %', NEW.cocinero_id, v_plato_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_validar_horario_reserva ON public.horarios;
CREATE TRIGGER trg_validar_horario_reserva
BEFORE INSERT OR UPDATE ON public.horarios
FOR EACH ROW EXECUTE FUNCTION public.validar_horario_reserva();

DROP TRIGGER IF EXISTS trg_validar_reservacion ON public.reservaciones;
CREATE TRIGGER trg_validar_reservacion
BEFORE INSERT OR UPDATE ON public.reservaciones
FOR EACH ROW EXECUTE FUNCTION public.validar_reservacion();

DROP TRIGGER IF EXISTS trg_validar_pedido_roles ON public.pedidos;
CREATE TRIGGER trg_validar_pedido_roles
BEFORE INSERT OR UPDATE ON public.pedidos
FOR EACH ROW EXECUTE FUNCTION public.validar_pedido_roles();

DROP TRIGGER IF EXISTS trg_validar_especialidad_cocinero ON public.especialidades;
CREATE TRIGGER trg_validar_especialidad_cocinero
BEFORE INSERT OR UPDATE ON public.especialidades
FOR EACH ROW EXECUTE FUNCTION public.validar_especialidad_cocinero();

DROP TRIGGER IF EXISTS trg_validar_preparacion_especialidad ON public.preparaciones;
CREATE TRIGGER trg_validar_preparacion_especialidad
BEFORE INSERT OR UPDATE ON public.preparaciones
FOR EACH ROW EXECUTE FUNCTION public.validar_preparacion_especialidad();

ALTER TABLE public.platos
ADD COLUMN spicy boolean NOT NULL DEFAULT false;

--
-- PostgreSQL database dump complete
--
