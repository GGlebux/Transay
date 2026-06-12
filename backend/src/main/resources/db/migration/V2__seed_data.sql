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

--
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.person (id, name, gender, date_of_birth, is_gravid) FROM stdin;
16	GGlebux	male	2006-08-16	f
\.


--
-- Data for Name: customer; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.customer (id, created_at, email, is_verified, password, role, status, person_id) FROM stdin;
1	2026-04-07 10:12:48.872256	admin@example.com	t	$2a$10$PKc73kG7Nvu7Lc2rAEzxReWzHoEgQ4HVUj.yKIdMo6msGGb63icIq	ADMIN	ACTIVE	\N
2	2026-04-07 14:13:25.22353	rejngleb@gmail.com	t	$2a$10$iQPHl/AgKTKFqEcaiFIqc.5KS.AqpovZqKeoT5gC5wtoGOCBMC/nG	USER	ACTIVE	16
\.


--
-- Data for Name: reason; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.reason (id, name) FROM stdin;
1	дефицит ванадия
2	передозировка рыбьего жира
3	избыточное поступление с воздухом меди
4	панкреатит
5	гипогонадизм
6	болезнь Пламмера
7	гиперфункциональность почек
8	нарушение работы поджелудочной железы
9	гипергидратация
10	киста желтого тела
11	показатель гипоксии
12	повышенное использование витамина в организме
13	сахарный диабет
14	переутомление
15	отсутствие сна
16	избыток белка
17	передозировка гормонов щитовидной железы
18	генетические мутации, нарушающие обмен В12 в организме
19	истинная полицитемия
20	низкий кортизол
21	низкое содержание йода в пище
22	искусственная гипогликемия
23	диффузный токсический зоб
24	передозировка витамина D
25	аллергия
26	применение тиреостатиков
27	нарушение работы надпочечников
28	скрытые кровопотери
29	ослабленный иммунитет
30	избыток метионина
31	стресс
32	высокий инсулин
33	язвы в желудке
34	нарушение работы гипоталамуса
35	гепатиты
36	явные кровопотери
37	доброкачественная нейтропения детского возраста
38	избыточное поступление с водой меди
39	передозировка витамина С
40	повышенная функция паращитовидных желез
41	гипохлоргидрия
42	тиреоидит Хашимото
43	синдром Пендреда
44	менструация
45	изменение регуляции метаболизма йода в организме
46	избыток гормона роста
47	дефицит эстрогена
48	нарушение работы печени
49	себорея
50	дефицит омега-3
51	дефицит белка
52	инъекции В12 в последние 2 месяца
53	повышение фибриногена
54	рвота
55	инфекционный процесс
56	дефицит кальция
57	дефицит витамина А
58	апластическая анемия
59	повышение ферритина
60	пониженная функция паращитовидных желез
61	приём эстрогенов
62	холестаз
63	снижение альбумина
64	ревматоидный артрит
65	дефицит йода
66	простатит
67	поликистоз яичников
68	прием ОК
69	лечение антибиотиками
70	дефицит витамина B6
71	дефицит витамина B9
72	нарушение образования внутреннего фактора Касла
73	прием антигипертензивных средств группы ИАПФ
74	дефицит витамина B2
75	повышение церулоплазмина
76	гемолиз эритроцитов при заборе крови
77	повышение эритроцитов
78	дефицит витамина B1
79	дисбактериоз
80	избыток поступления с пищей фосфора
81	нарушение всасываемости
82	гипохромная анемия
83	курение
84	вирусная инфекция
85	послеродовый тиреоидит
86	прием препаратов с высокими дозами В12
87	высокий уровень окислительного стресса
88	целиакия
89	гормональные нарушения
90	дефицит витамина C
91	приём КОК
92	недостаточность надпочечников
93	беременность
94	дефицит глутатиона
95	недостаток меди в пище
96	алкалоз
97	гиперхромная анемия
98	дефицит витамина D
99	паразитоз
100	первые 6-8 недель терапии препаратами железа
101	диарея
102	недостаточное поступление с пищей
103	пожилой возраст
104	приём глюкокортикоидов
105	талассемия
106	воспалительный процесс
107	избыток пролактина
108	дефицит инозитола
109	прием лекарств для снижения давления
110	почечная недостаточность
111	повышение альдостерона
112	вегетарианство
113	снижение эритроцитов
114	дефицит селена
115	избыток андрогенов
116	гематологические заболевания
117	лимфома
118	опухоли
119	повышенное потоотделение
120	изменение формы эритроцитов
121	низкий уровень ДГЭА-С
122	метаболический синдром
123	нарушение работы почек
124	катаболизм
125	бактериальная инфекция
126	дефицит бетаина
127	инфекция Helicobacter pylori
128	увеличение селезёнки
129	бессолевая диета
130	состояние кетоза
131	прием стероидных гормонов
132	избыток кофе в рационе
133	повышение альбумина
135	дефицит метионина
136	дефицит витамина B12
137	атеросклероз
138	нарушение обмена веществ
139	приём некоторых лекарственных препаратов
140	период активного роста у ребёнка
141	врождённый дефицит лептина
142	мутация гена MTHFR
143	дефицит цинка
144	несбалансированная диета
145	псориаз
146	гемолитическая анемия
147	голодание
148	пониженное выделение кальция с мочой
149	обезвоживание
150	нарушение работы щитовидной железы
151	дефицит железа
152	избыток кальция
153	избыток фитоэстрогенов в рационе
154	избыток алюминия
155	длительный приём препаратов с магнием
156	дробное питание
157	дефицит марганца
158	микросфероцитоз
159	нарушение работы желчного системы
160	мальабсорбция
161	гиперфункциональность кишечника
162	нарушение работы гипофиза
163	злоупотребление БАДами
164	гипертиреоз
165	гиперпротеинемия
166	дефицит холина
167	генетика
168	дефицит фолатов
169	резкая потеря веса
170	прием пищи перед проведением анализа крови
171	повышение С-реактивного белка
172	аутоиммунный тиреоидит
173	избыток железа
174	нарушение работы эндокринной системы
175	инсулинорезистентность
176	недостаток в пище B9
177	гипергликемия
178	лейкоз
179	ацидоз
180	синдром Жильбера
181	избыток углеводов в рационе
182	дефицит хрома
183	снижение гематокрита
184	резистентность к андрогенам
185	высокий кортизол
186	эрозии в желудке
187	малоподвижный образ жизни
188	сердечная недостаточность
189	дефицит фосфора
190	болезни ЖКТ
191	глистная инвазия
192	заболевания крови
193	недостаточность желтого тела
194	гемохроматоз
195	избыток гомоцистеина
196	лептинорезистентность
197	увеличение содержания йода в пище
198	проблемы с усвоением железа в кишечнике
199	дефицит половых гормонов
200	избыточное поступление с воздухом цинка
201	дефицит меди
202	нарушение внутриклеточного обмена
203	гемахроматоз
204	избыток мясной пищи в рационе
205	дефицит магния
206	интоксикация
207	приём мочегонных препаратов
208	ожирение
209	гипопаратиреоз
210	передозировка витаминов фолиевой кислоты
211	нарушение работы тонкого кишечника
212	повышенный холестерин
213	гипотиреоз
214	новообразования
215	токсичные металлы
216	остеопороз
217	пониженная кислотность желудка
218	дефицит жиров в рационе
219	злоупотребление алкоголем
220	нарушения всасывания жиров
221	высокое артериальное давление
222	синдром раздраженного кишечника
223	микроцитарная анемия
224	избыток жидкости
134	физическоe перенапряжении
225	гипогликемия
227	анализ сдан некорректно
228	ускоренный распад эритроцитов
226	длительный приём аскорбиновой кислоты
229	тиреотоксикоз
236	недостаток потребления воды
\.


--
-- Data for Name: excluded_reason; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.excluded_reason (person_id, reason_id) FROM stdin;
16	44
16	67
16	93
\.


--
-- Data for Name: transcript; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.transcript (id, name, gender) FROM stdin;
6	erythrocyte	both
7	average volume of erythrocyte	both
10	average hemoglobin content in erythrocyte	both
11	average concentration Hb in erythrocyte	both
14	range between large and small erythrocyte	both
5	hemoglobin	both
15	hematocrit	both
16	platelets	both
17	average platelet volume	both
18	leukocyte	both
19	neutrophils	both
20	absolute number of neutrophils	both
21	eosinophils	both
22	absolute number of eosinophils	both
23	basophils	both
24	absolute number of basophils	both
25	lymphocytes	both
26	absolute number of lymphocytes	both
27	monocytes	both
28	absolute number of monocytes	both
29	erythrocyte sedimentation rate	both
30	ferritin	both
32	transferrin	both
33	saturation of transferrin with iron	both
34	concentration of iron in the blood serum	both
35	total iron binding capacity of serum	both
36	homocysteine	both
37	C-peptide	both
39	C-reactive protein	both
40	fibrinogen	both
41	protein total	both
42	albumin	both
43	blood creatinine	both
44	urine creatinine	both
45	urea	both
46	uric acid	both
47	fasting blood glucose	both
48	glycated hemoglobin	both
49	fasting insulin	both
50	total cholesterol	both
51	high-density lipoproteins	both
52	low-density lipoproteins	both
53	very low density lipoproteins	both
54	triglycerides	both
55	coefficient of atherogenicity	both
56	aspartate aminotransferase	both
57	alanine aminotransferase	both
58	pancreatic amylase	both
59	alpha-amylase	both
60	alkaline phosphatase	both
61	gamma-glutamyltransferase	both
62	total bilirubin	both
63	direct bilirubin	both
64	relative width of erythrocyte distribution by volume	both
65	indirect bilirubin	both
66	thyroid-stimulating hormone	both
67	free T4	both
68	free T3	both
69	T3 reversible	both
70	antibodies TPO and TG	both
71	total testosterone	male
72	total testosterone	female
73	free testosterone	female
74	free testosterone	male
75	biologically available testosterone	male
76	biologically available testosterone	female
77	index of free androgens	both
78	free cortisol urine	both
79	DHEA-S	both
80	progesterone	both
81	progesterone	female
82	sex hormone-binding globulin	both
83	estradiol	male
84	estradiol	female
85	leptin	both
86	vitamin D	both
87	cobalamin	both
88	holotranscobalamin	both
89	folic acid in blood serum	both
90	folic acid in red blood cells	both
91	vitamin B6	both
92	copper in urine	both
93	copper in blood	both
94	ceruloplasmin	both
95	zinc in blood	both
96	magnesium	both
97	iodine in urine	both
98	iodine in hair	both
99	potassium	both
100	total calcium	both
101	ionized calcium	both
102	phosphorus	both
103	sodium	both
104	chloride	female
\.


--
-- Data for Name: fall_reason; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.fall_reason (transcript_id, reason_id) FROM stdin;
5	28
5	33
5	36
5	51
5	71
5	78
5	90
5	93
5	127
5	136
5	151
5	157
5	186
5	201
6	70
6	71
6	93
6	106
6	123
6	124
6	136
6	215
7	41
7	70
7	90
7	146
7	151
7	201
10	41
10	70
10	90
10	93
10	146
10	151
10	201
11	41
11	64
11	70
11	90
11	146
11	151
11	201
14	64
14	70
14	100
14	146
14	151
14	223
15	9
15	28
15	36
15	58
15	82
15	93
15	97
15	146
15	223
16	44
16	71
16	84
16	93
16	136
16	150
17	48
17	58
17	128
18	27
18	31
18	84
18	136
18	151
18	213
19	37
19	84
20	37
20	84
23	55
23	71
23	134
23	136
24	55
24	71
24	134
24	136
25	29
25	51
25	84
26	29
26	51
26	84
27	29
27	136
28	29
28	136
29	120
29	133
29	149
29	170
29	179
30	93
30	150
30	151
32	51
32	143
33	151
34	102
34	198
35	51
35	55
35	105
35	125
35	136
35	146
35	173
35	203
36	93
36	94
36	135
37	13
37	22
40	2
40	77
40	90
40	93
40	112
40	136
40	144
41	48
41	93
41	112
41	123
41	133
41	144
41	151
41	217
42	48
42	55
42	84
42	93
42	188
42	224
43	9
43	51
43	93
43	147
44	9
44	51
44	93
44	147
45	9
45	48
45	51
45	93
45	150
46	7
46	48
46	51
46	93
46	161
47	225
48	225
49	130
50	151
50	157
50	164
50	167
50	190
50	218
50	220
51	50
51	88
51	202
52	218
52	220
53	218
53	220
54	48
54	164
54	218
54	220
55	14
55	112
55	144
55	147
55	134
56	8
56	48
56	70
57	8
57	48
57	70
58	8
58	212
59	8
59	212
60	24
60	90
60	143
60	168
60	201
60	205
60	209
60	213
61	139
61	213
61	226
62	51
62	110
62	151
62	227
63	69
63	104
63	219
64	64
64	70
64	151
64	223
66	150
67	93
67	147
67	169
68	26
68	65
68	114
68	150
68	213
71	5
71	66
71	104
71	107
71	112
71	208
71	218
72	5
72	92
72	104
72	112
72	218
73	5
73	92
73	104
73	112
73	218
74	5
74	66
74	104
74	107
74	112
74	208
74	218
75	5
75	66
75	104
75	107
75	112
75	208
75	218
76	5
76	92
76	104
76	112
76	218
77	5
77	66
78	14
78	15
78	27
78	48
78	213
79	27
79	31
79	67
79	68
79	208
80	34
80	107
80	115
80	150
80	162
80	193
81	175
82	13
82	48
82	67
82	107
82	115
82	123
82	208
83	14
83	83
83	174
84	174
84	175
84	218
85	141
85	147
86	48
86	103
86	123
86	151
86	175
86	187
87	18
87	72
87	81
87	99
87	102
87	103
88	18
88	72
88	81
88	99
88	102
88	103
89	88
89	102
89	142
89	160
89	219
90	88
90	102
90	142
90	160
90	219
91	81
91	93
91	102
92	48
92	123
92	160
92	216
93	48
93	123
93	160
93	216
94	51
94	95
94	160
95	13
95	31
95	49
95	55
95	93
95	105
95	145
95	150
95	160
96	13
96	14
96	16
96	54
96	93
96	101
96	175
96	208
97	21
97	42
97	45
97	150
98	21
98	42
98	45
98	150
99	54
99	101
99	111
99	119
99	131
99	207
100	93
100	216
101	60
101	96
101	98
101	205
102	51
102	57
102	98
102	138
102	152
102	154
102	155
103	54
103	92
103	101
\.


--
-- Data for Name: indicator; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.indicator (id, eng_name, rus_name, gender, is_gravid, min_age, max_age, min_value, max_value, units) FROM stdin;
1	hemoglobin	гемоглобин	both	f	0	6	180	220	г/л
34	erythrocyte	эритроциты	both	f	0	13	3.9	6	тера/л
35	erythrocyte	эритроциты	both	f	14	29	3.5	5.5	тера/л
37	erythrocyte	эритроциты	both	f	30	179	3.5	4	тера/л
39	erythrocyte	эритроциты	both	f	365	4379	4.5	5	тера/л
38	erythrocyte	эритроциты	both	f	180	364	4	5	тера/л
40	erythrocyte	эритроциты	female	f	4380	72999	4.3	4.9	тера/л
41	erythrocyte	эритроциты	male	f	4380	72999	4.5	5.5	тера/л
42	average volume of erythrocyte	средний объём эритроцитов	both	f	0	29	98	118	фл
43	average volume of erythrocyte	средний объём эритроцитов	both	f	30	89	88	100	фл
44	average volume of erythrocyte	средний объём эритроцитов	both	f	90	179	85	95	фл
45	average volume of erythrocyte	средний объём эритроцитов	both	f	180	729	80	88	фл
46	average volume of erythrocyte	средний объём эритроцитов	both	f	730	4379	85	90	фл
47	average volume of erythrocyte	средний объём эритроцитов	female	f	4380	72999	88	93	фл
48	average volume of erythrocyte	средний объём эритроцитов	male	f	4380	72999	88	95	фл
49	average hemoglobin content in erythrocyte	среднее содержание гемоглобина в эритроците	both	f	0	29	32	37	пг
50	average hemoglobin content in erythrocyte	среднее содержание гемоглобина в эритроците	both	f	30	179	28	36	пг
51	average hemoglobin content in erythrocyte	среднее содержание гемоглобина в эритроците	both	f	180	729	25	28	пг
53	average hemoglobin content in erythrocyte	среднее содержание гемоглобина в эритроците	both	f	730	1824	26	34	пг
54	average hemoglobin content in erythrocyte	среднее содержание гемоглобина в эритроците	both	f	1825	72999	28	32	пг
55	average concentration Hb in erythrocyte	средняя концентрация Hb в эритроцитах	both	f	0	72999	320	359	г/л
56	relative width of erythrocyte distribution by volume	относительная ширина распределения эритроцитов по объёму	both	f	0	364	11.6	14.8	%
58	range between large and small erythrocyte	диапазон между большим и маленьким эритроцитом	both	f	0	72999	37	47	фл
28	hemoglobin	гемоглобин	both	f	7	29	150	180	г/л
30	hemoglobin	гемоглобин	both	f	180	1824	120	140	г/л
33	hemoglobin	гемоглобин	male	f	4380	72999	130	170	г/л
32	hemoglobin	гемоглобин	female	f	4380	72999	125	160	г/л
29	hemoglobin	гемоглобин	both	f	30	179	90	120	г/л
31	hemoglobin	гемоглобин	both	f	1825	4379	125	150	г/л
59	hematocrit	гематокрит	both	f	180	729	33	39	%
60	hematocrit	гематокрит	both	f	730	2189	34	40	%
61	hematocrit	гематокрит	both	f	2190	4379	35	45	%
62	hematocrit	гематокрит	female	f	4380	6569	36	46	%
63	hematocrit	гематокрит	male	f	4380	6569	37	49	%
64	hematocrit	гематокрит	female	f	6570	72999	36	47	%
65	hematocrit	гематокрит	male	f	6570	72999	40	54	%
66	platelets	тромбоциты	both	f	0	72999	180	399	*10^(9)/л
67	average platelet volume	средний объём тромбоцитов	both	f	0	364	7	7.9	фл
68	average platelet volume	средний объём тромбоцитов	both	f	365	1824	8	8.8	фл
69	average platelet volume	средний объём тромбоцитов	both	f	1825	72999	7	10	фл
70	leukocyte	лейкоциты	both	f	0	364	8	12	*10^(9)/л
71	leukocyte	лейкоциты	both	f	365	1094	7	11	*10^(9)/л
72	leukocyte	лейкоциты	both	f	1095	3649	6	10	*10^(9)/л
73	leukocyte	лейкоциты	both	f	3650	5474	5	9	*10^(9)/л
74	leukocyte	лейкоциты	female	f	5475	72999	4	10	*10^(9)/л
75	leukocyte	лейкоциты	male	f	5475	72999	4	9	*10^(9)/л
76	neutrophils	нейтрофилы	both	f	0	364	16	45	%
77	neutrophils	нейтрофилы	both	f	365	729	28	48	%
78	neutrophils	нейтрофилы	both	f	730	1824	32	55	%
79	neutrophils	нейтрофилы	both	f	1825	2919	40	60	%
80	neutrophils	нейтрофилы	both	f	2920	5474	45	60	%
81	neutrophils	нейтрофилы	both	f	5475	72999	45	74	%
82	absolute number of neutrophils	абсолютное количество нейтрофилов	both	f	0	364	1	8.5	*10^(9)/л
83	absolute number of neutrophils	абсолютное количество нейтрофилов	both	f	365	1824	1.5	8.5	*10^(9)/л
84	absolute number of neutrophils	абсолютное количество нейтрофилов	both	f	1825	5474	1.8	8	*10^(9)/л
85	absolute number of neutrophils	абсолютное количество нейтрофилов	both	f	5475	72999	2	6	*10^(9)/л
86	eosinophils	эозинофилы	both	f	0	72999	0	2	%
87	absolute number of eosinophils	абсолютное количество эозинофилов	both	f	0	72999	0	0.5	*10^(9)/л
88	basophils	базофилы	both	f	0	72999	0	1	%
89	absolute number of basophils	абсолютное количество базофилов	both	f	0	72999	0	0.1	*10^(9)/л
90	lymphocytes	лимфоциты	both	f	365	729	37	60	%
91	lymphocytes	лимфоциты	both	f	730	1824	33	55	%
92	lymphocytes	лимфоциты	both	f	1825	2919	30	50	%
93	lymphocytes	лимфоциты	both	f	2920	5474	30	45	%
94	lymphocytes	лимфоциты	both	f	5475	72999	18	40	%
95	lymphocytes	лимфоциты	both	f	0	364	46	65	%
96	absolute number of lymphocytes	абсолютное количество лимфоцитов	both	f	0	364	2	11	*10^(9)/л
97	absolute number of lymphocytes	абсолютное количество лимфоцитов	both	f	365	729	3	9.5	*10^(9)/л
98	absolute number of lymphocytes	абсолютное количество лимфоцитов	both	f	730	1824	2	8	*10^(9)/л
99	absolute number of lymphocytes	абсолютное количество лимфоцитов	both	f	1825	2919	1.5	6.8	*10^(9)/л
100	absolute number of lymphocytes	абсолютное количество лимфоцитов	both	f	2920	5474	1.2	5.2	*10^(9)/л
101	absolute number of lymphocytes	абсолютное количество лимфоцитов	both	f	5475	72999	1.2	4.5	*10^(9)/л
102	monocytes	моноциты	both	f	0	364	5	12	%
103	monocytes	моноциты	both	f	365	1459	4	10	%
104	monocytes	моноциты	both	f	1460	5839	3	9	%
105	monocytes	моноциты	both	f	5840	72999	2	8	%
106	absolute number of monocytes	абсолютное количество моноцитов	both	f	0	4379	0.05	1.1	*10^(9)/л
107	absolute number of monocytes	абсолютное количество моноцитов	both	f	4380	6569	0.04	0.8	*10^(9)/л
108	absolute number of monocytes	абсолютное количество моноцитов	both	f	6570	72999	0.08	0.6	*10^(9)/л
109	erythrocyte sedimentation rate	скорость оседания эритроцитов	both	f	0	29	1	2	мм/час
110	erythrocyte sedimentation rate	скорость оседания эритроцитов	both	f	30	149	2	4	мм/час
111	erythrocyte sedimentation rate	скорость оседания эритроцитов	both	f	150	364	4	8	мм/час
112	erythrocyte sedimentation rate	скорость оседания эритроцитов	both	f	365	4379	4	12	мм/час
113	erythrocyte sedimentation rate	скорость оседания эритроцитов	female	f	4380	18249	3	15	мм/час
114	erythrocyte sedimentation rate	скорость оседания эритроцитов	female	f	18250	72999	2	30	мм/час
115	erythrocyte sedimentation rate	скорость оседания эритроцитов	male	f	4380	18249	2	10	мм/час
116	erythrocyte sedimentation rate	скорость оседания эритроцитов	male	f	18250	72999	2	20	мм/час
117	ferritin	ферритин	both	f	0	179	70	300	мкг/л
118	ferritin	ферритин	both	f	180	5474	50	100	мкг/л
119	ferritin	ферритин	female	f	5475	72999	50	100	мкг/л
120	ferritin	ферритин	male	f	5475	72999	70	150	мкг/л
122	transferrin	трансферрин	both	f	0	729	2	3.5	г/л
123	transferrin	трансферрин	both	f	730	72999	2	3	г/л
124	saturation of transferrin with iron	насыщение трансферрина железом	both	f	0	72999	30	40	%
125	concentration of iron in the blood serum	концентрация железа в сыворотке крови	both	f	0	72999	15	30	мкмоль/л
126	total iron binding capacity of serum	общая железосвязывающая способность сыворотки	both	f	0	729	50	65	мкмоль/л
127	total iron binding capacity of serum	общая железосвязывающая способность сыворотки	both	f	730	72999	45	55	мкмоль/л
133	C-peptide	С-пептид	both	f	0	72634	2.5	3	нг/мл
134	C-reactive protein	С-реактивный белок	both	f	0	6569	0	1.6	мг/л
135	C-reactive protein	С-реактивный белок	female	f	6570	72999	0	1	мг/л
136	C-reactive protein	С-реактивный белок	male	f	6570	72999	0	0.55	мг/л
137	fibrinogen	фибриноген	both	f	0	364	1.25	3	г/л
138	fibrinogen	фибриноген	both	f	365	72999	2	4	г/л
139	protein total	общий белок	female	f	0	364	51	73	г/л
140	protein total	общий белок	female	f	365	7299	62	76	г/л
141	protein total	общий белок	female	f	7300	12409	75	82	г/л
142	protein total	общий белок	female	f	12410	21899	76	80	г/л
143	protein total	общий белок	female	f	21900	72999	74	78	г/л
144	protein total	общий белок	male	f	0	364	51	73	г/л
145	protein total	общий белок	male	f	365	7299	62	79	г/л
146	protein total	общий белок	male	f	7300	12409	75	79	г/л
147	protein total	общий белок	male	f	12410	21899	79	83	г/л
128	homocysteine	гомоцистеин	both	f	0	4379	5	7	мкмоль/л
129	homocysteine	гомоцистеин	female	f	4380	6934	3.3	7.2	мкмоль/л
130	homocysteine	гомоцистеин	male	f	4380	6934	4.3	9.9	мкмоль/л
131	homocysteine	гомоцистеин	female	f	21900	72999	4.9	11.6	мкмоль/л
132	homocysteine	гомоцистеин	male	f	21900	72999	5.9	15.3	мкмоль/л
148	protein total	общий белок	male	f	21900	72999	74	78	г/л
149	albumin	альбумин	both	f	0	5109	45	54	г/л
150	albumin	альбумин	both	f	5110	21899	40	52	г/л
151	albumin	альбумин	both	f	21900	72999	35	46	г/л
156	urine creatinine	креатинин в моче	both	f	0	72999	1	1.6	г/сут
157	urea	мочевина	both	f	0	5109	1.8	4.9	мкмоль/л
158	urea	мочевина	female	f	5110	72999	2.3	6.6	мкмоль/л
159	urea	мочевина	male	f	5110	72999	2.4	6.5	мкмоль/л
160	uric acid	мочевая кислота	both	f	0	5109	120	290	мкмоль/л
162	uric acid	мочевая кислота	female	f	5110	72999	160	320	мкмоль/л
163	uric acid	мочевая кислота	male	f	5110	72999	200	400	мкмоль/л
164	fasting blood glucose	глюкоза в крови натощак	both	f	0	1824	3.3	5	ммоль/л
165	fasting blood glucose	глюкоза в крови натощак	both	f	1825	72999	3.9	5.5	ммоль/л
166	glycated hemoglobin	гликированный гемоглобин	both	f	0	72999	4.6	5.5	%
167	fasting insulin	инсулин натощак	both	f	0	72999	2	6	МкЕд/мл
168	total cholesterol	общий холестерин	both	f	0	4379	3.12	5.17	ммоль/л
169	total cholesterol	общий холестерин	both	f	4380	6934	3.12	5.43	ммоль/л
170	total cholesterol	общий холестерин	both	f	6935	16424	3.63	5.2	ммоль/л
171	total cholesterol	общий холестерин	both	f	16425	27374	3.63	6	ммоль/л
172	total cholesterol	общий холестерин	both	f	27375	72999	3.63	7	ммоль/л
173	high-density lipoproteins	липопротеины высокой плотности	both	f	0	4379	0.78	1.68	ммоль/л
174	high-density lipoproteins	липопротеины высокой плотности	both	f	4380	6569	0.9	1.9	ммоль/л
175	high-density lipoproteins	липопротеины высокой плотности	female	f	6570	72999	1.29	1.8	ммоль/л
176	high-density lipoproteins	липопротеины высокой плотности	male	f	6570	72999	1.03	1.8	ммоль/л
177	low-density lipoproteins	липопротеины низкой плотности	both	f	0	4379	1.55	3.63	ммоль/л
178	low-density lipoproteins	липопротеины низкой плотности	both	f	4380	72999	2.6	3.89	ммоль/л
179	very low density lipoproteins	липопротеины очень низкой плотности	both	f	0	72999	0	0.5	ммоль/л
180	triglycerides	триглицериды	both	f	0	364	0.2	0.86	ммоль/л
181	triglycerides	триглицериды	both	f	365	72999	0.41	1	ммоль/л
183	aspartate aminotransferase	аспартатаминотрансфераза	both	f	0	179	20	84	ме/л
184	aspartate aminotransferase	аспартатаминотрансфераза	both	f	180	364	20	89	ме/л
185	aspartate aminotransferase	аспартатаминотрансфераза	both	f	365	1094	20	56	ме/л
186	aspartate aminotransferase	аспартатаминотрансфераза	both	f	1095	2189	20	39	ме/л
187	aspartate aminotransferase	аспартатаминотрансфераза	both	f	2190	4379	20	38	ме/л
188	aspartate aminotransferase	аспартатаминотрансфераза	both	f	4380	6204	20	35	ме/л
189	aspartate aminotransferase	аспартатаминотрансфераза	female	f	6205	72999	20	35	ме/л
190	aspartate aminotransferase	аспартатаминотрансфераза	male	f	6205	72999	20	41	ме/л
191	alanine aminotransferase	аланинаминотрансфераза	both	f	0	179	20	60	ме/л
192	alanine aminotransferase	аланинаминотрансфераза	both	f	180	364	20	57	ме/л
194	alanine aminotransferase	аланинаминотрансфераза	both	f	365	1094	20	39	ме/л
195	alanine aminotransferase	аланинаминотрансфераза	both	f	1095	2189	20	29	ме/л
196	alanine aminotransferase	аланинаминотрансфераза	both	f	2190	4379	20	37	ме/л
197	alanine aminotransferase	аланинаминотрансфераза	both	f	4380	6204	20	26	ме/л
199	alanine aminotransferase	аланинаминотрансфераза	male	f	6205	72999	20	33	ме/л
198	alanine aminotransferase	аланинаминотрансфераза	female	f	6205	72999	20	25	ме/л
200	pancreatic amylase	панкреатическая амилаза	both	f	0	179	1	12	ед/л
201	pancreatic amylase	панкреатическая амилаза	both	f	180	364	1	23	ед/л
202	pancreatic amylase	панкреатическая амилаза	female	f	365	729	3	38	ед/л
203	pancreatic amylase	панкреатическая амилаза	male	f	365	729	1	23	ед/л
204	pancreatic amylase	панкреатическая амилаза	both	f	730	6569	4	31	ед/л
205	pancreatic amylase	панкреатическая амилаза	both	f	6570	72999	25	55	ед/л
206	alpha-amylase	альфа-амилаза	both	f	0	729	5	65	ед/л
207	alpha-amylase	альфа-амилаза	both	f	730	72999	25	100	ед/л
208	alkaline phosphatase	щелочная фосфатаза	both	f	0	9	150	380	ед/л
153	blood creatinine	креатинин в крови	both	f	4380	6569	44	88	мкмоль/л
209	alkaline phosphatase	щелочная фосфатаза	both	f	10	364	130	700	ед/л
210	alkaline phosphatase	щелочная фосфатаза	both	f	365	1094	350	600	ед/л
211	alkaline phosphatase	щелочная фосфатаза	both	f	1095	3284	400	700	ед/л
212	alkaline phosphatase	щелочная фосфатаза	both	f	3285	6569	155	500	ед/л
213	alkaline phosphatase	щелочная фосфатаза	female	f	6570	10949	80	90	ед/л
214	alkaline phosphatase	щелочная фосфатаза	female	f	10950	16424	90	100	ед/л
215	alkaline phosphatase	щелочная фосфатаза	female	f	16425	19709	105	115	ед/л
216	alkaline phosphatase	щелочная фосфатаза	female	f	19710	72999	125	135	ед/л
217	alkaline phosphatase	щелочная фосфатаза	male	f	6570	10949	100	110	ед/л
218	alkaline phosphatase	щелочная фосфатаза	male	f	10950	16424	110	120	ед/л
219	alkaline phosphatase	щелочная фосфатаза	male	f	16425	19709	120	130	ед/л
220	alkaline phosphatase	щелочная фосфатаза	male	f	19710	72999	135	145	ед/л
221	gamma-glutamyltransferase	гамма-глутамилтрансфераза	both	f	0	179	15	200	ед/л
222	gamma-glutamyltransferase	гамма-глутамилтрансфераза	both	f	180	364	15	35	ед/л
223	gamma-glutamyltransferase	гамма-глутамилтрансфераза	both	f	365	2189	15	23	ед/л
225	gamma-glutamyltransferase	гамма-глутамилтрансфераза	both	f	2190	4379	15	17	ед/л
226	gamma-glutamyltransferase	гамма-глутамилтрансфераза	female	f	4380	6569	15	30	ед/л
227	gamma-glutamyltransferase	гамма-глутамилтрансфераза	male	f	4380	6569	15	45	ед/л
228	gamma-glutamyltransferase	гамма-глутамилтрансфераза	female	f	6570	72999	15	32	ед/л
229	gamma-glutamyltransferase	гамма-глутамилтрансфераза	male	f	6570	72999	15	50	ед/л
230	total bilirubin	билирубин общий	both	f	0	1	10	23.1	мкмоль/л
231	total bilirubin	билирубин общий	both	f	2	3	10	54.2	мкмоль/л
232	total bilirubin	билирубин общий	both	f	4	5	10	90.1	мкмоль/л
233	total bilirubin	билирубин общий	both	f	6	8	10	53	мкмоль/л
234	total bilirubin	билирубин общий	both	f	9	5109	3.4	13.7	мкмоль/л
236	total bilirubin	билирубин общий	female	f	5110	72999	3.2	17	мкмоль/л
237	total bilirubin	билирубин общий	male	f	5110	72999	3.4	17.1	мкмоль/л
238	direct bilirubin	билирубин прямой	both	f	0	1	1.5	8.7	мкмоль/л
239	direct bilirubin	билирубин прямой	both	f	2	3	1.5	7.9	мкмоль/л
240	direct bilirubin	билирубин прямой	both	f	4	5	1.5	8.7	мкмоль/л
243	direct bilirubin	билирубин прямой	both	f	6	5109	0.86	3.4	мкмоль/л
244	direct bilirubin	билирубин прямой	female	f	5110	72999	1.5	4.7	мкмоль/л
245	direct bilirubin	билирубин прямой	male	f	5110	72999	1.7	5.1	мкмоль/л
246	eosinophilic cationic protein	эозинофильный катионный белок	both	f	0	72999	0	24	мкг/л
247	relative width of erythrocyte distribution by volume	относительная ширина распределения эритроцитов по объёму	both	f	365	72999	11	13	%
182	coefficient of atherogenicity	коэффициент атерогенности	both	f	0	72999	2	3	ме/л
249	indirect bilirubin	билирубин непрямой	both	f	0	1	0	14.4	мкмоль/л
250	indirect bilirubin	билирубин непрямой	both	f	2	3	0	45.5	мкмоль/л
251	indirect bilirubin	билирубин непрямой	both	f	4	5	0	82.3	мкмоль/л
253	indirect bilirubin	билирубин непрямой	both	f	9	29	0	44.3	мкмоль/л
252	indirect bilirubin	билирубин непрямой	both	f	6	8	0	63.3	мкмоль/л
254	indirect bilirubin	билирубин непрямой	both	f	30	5109	2.57	10.3	мкмоль/л
255	indirect bilirubin	билирубин непрямой	female	f	5110	72999	3.2	12	мкмоль/л
258	thyroid-stimulating hormone	тиреотропный гормон	both	f	6570	21899	0.5	2	мМЕ/л
261	free T4	Т4 свободный	both	f	0	72999	15	22	пмоль/л
263	free T3	Т3 свободный	both	f	0	72999	5	7	пмоль/л
257	thyroid-stimulating hormone	тиреотропный гормон	both	f	0	6569	0.4	2.5	мМЕ/л
256	indirect bilirubin	билирубин непрямой	male	f	5110	72999	3.5	12.5	мкмоль/л
260	free T4	Т4 свободный	both	f	0	72999	1.16	1.7	нг/дл
259	thyroid-stimulating hormone	тиреотропный гормон	both	f	21900	72999	0.5	2.5	мМЕ/л
264	T3 reversible	Т3 реверсивный	both	f	0	72999	11	18	нг/дл
262	free T3	Т3 свободный	both	f	0	72999	3.25	4.55	пг/мл
268	antibodies TPO and TG	антитела к ТПО и ТГ	both	f	0	72999	0	2	МЕ/мл
248	homocysteine	гомоцистеин	both	f	6935	21899	5	7	мкмоль/л
287	biologically available testosterone	биологически доступный тестостерон	male	f	6570	18249	3.68	15.3	нмоль/л
288	biologically available testosterone	биологически доступный тестостерон	male	f	18250	72999	3.07	12.6	нмоль/л
289	biologically available testosterone	биологически доступный тестостерон	female	f	6570	18249	0.033	0.774	нмоль/л
290	biologically available testosterone	биологически доступный тестостерон	female	f	18250	72999	0.2	0.46	нмоль/л
291	index of free androgens	индекс свободных андрогенов	male	f	6570	72999	40.1	95	%
296	total testosterone	тестостерон общий	male	f	0	2919	0	0.5	нмоль/л
297	total testosterone	тестостерон общий	male	f	2920	3649	0.2	0.7	нмоль/л
298	total testosterone	тестостерон общий	male	f	3650	4014	0.2	1.2	нмоль/л
299	total testosterone	тестостерон общий	male	f	4015	4379	0.1	5.4	нмоль/л
300	total testosterone	тестостерон общий	male	f	4380	5109	0.4	9.5	нмоль/л
301	total testosterone	тестостерон общий	male	f	5110	5839	1	16.3	нмоль/л
302	total testosterone	тестостерон общий	male	f	5840	6569	1.7	27.7	нмоль/л
303	total testosterone	тестостерон общий	female	f	0	2919	0.1	0.3	нмоль/л
304	total testosterone	тестостерон общий	female	f	2920	4379	0.2	0.5	нмоль/л
305	total testosterone	тестостерон общий	female	f	4380	5109	0.2	1	нмоль/л
306	total testosterone	тестостерон общий	female	f	5110	6569	0.6	1.6	нмоль/л
283	free testosterone	тестостерон свободный	female	f	6570	18249	0.001	0.034	нмоль/л
284	free testosterone	тестостерон свободный	female	f	18250	72999	0.001	0.022	нмоль/л
286	free testosterone	тестостерон свободный	male	f	18250	72999	0.13	0.6	нмоль/л
285	free testosterone	тестостерон свободный	male	f	6570	18249	0.2	0.9	нмоль/л
308	prolactin	пролактин	both	f	0	4	120	496	нг/мл
309	prolactin	пролактин	both	f	5	729	5.3	63.3	нг/мл
310	prolactin	пролактин	both	f	730	1459	4.4	29.7	нг/мл
311	prolactin	пролактин	both	f	1460	4379	2.63	21	нг/мл
312	prolactin	пролактин	female	f	4380	5109	2.52	16.9	нг/мл
313	prolactin	пролактин	male	f	4380	5109	2.84	24	нг/мл
314	prolactin	пролактин	female	f	5110	6569	4.2	39	нг/мл
315	prolactin	пролактин	male	f	5110	6569	2.76	16.1	нг/мл
316	prolactin	пролактин	both	f	6570	72999	4	15	нг/мл
318	free cortisol urine	свободный кортизол в суточной моче	both	f	0	72999	110	192	нмоль/сут
319	DHEA-S	ДГЭА-С	male	f	0	2919	0	0.6	мкмоль/л
320	DHEA-S	ДГЭА-С	male	f	2920	3649	0.2	2.8	мкмоль/л
321	DHEA-S	ДГЭА-С	male	f	3650	4379	0.9	3.8	мкмоль/л
322	DHEA-S	ДГЭА-С	male	f	4380	6569	2	10	мкмоль/л
323	DHEA-S	ДГЭА-С	female	f	0	2919	0	0.6	мкмоль/л
325	DHEA-S	ДГЭА-С	female	f	2920	3649	0.3	1.6	мкмоль/л
326	DHEA-S	ДГЭА-С	female	f	3650	4379	0.8	3.2	мкмоль/л
327	DHEA-S	ДГЭА-С	female	f	4380	5109	1	5	мкмоль/л
328	DHEA-S	ДГЭА-С	female	f	5110	6569	3	12	мкмоль/л
329	DHEA-S	ДГЭА-С	female	f	6570	72999	6	14	мкмоль/л
330	DHEA-S	ДГЭА-С	male	f	6570	72999	6	21	мкмоль/л
331	progesterone	прогестерон	male	f	0	4379	0.2	3.5	нмоль/л
332	progesterone	прогестерон	male	f	4380	5839	0.3	4.2	нмоль/л
333	progesterone	прогестерон	male	f	5840	6569	0.5	5	нмоль/л
334	progesterone	прогестерон	male	f	6570	72999	1	5.4	нмоль/л
335	progesterone	прогестерон	male	f	6570	72999	36	179	нг/дл
337	progesterone	прогестерон	female	f	0	1459	0.3	3.7	нмоль/л
338	progesterone	прогестерон	female	f	1460	2919	0.3	2.9	нмоль/л
340	progesterone	прогестерон	female	f	4380	6569	0.3	3.4	нмоль/л
339	progesterone	прогестерон	female	f	2920	4379	0.3	4.3	нмоль/л
341	progesterone	прогестерон	female	f	6570	72999	0.8	6.5	нмоль/л
342	progesterone	прогестерон	female	f	6570	72999	27	217	нг/дл
343	sex hormone-binding globulin	глобулин, связывающий половые гормоны	female	f	6570	72999	50	71	нмоль/л
344	sex hormone-binding globulin	глобулин, связывающий половые гормоны	male	f	6570	72999	51	68	нмоль/л
345	estradiol	эстрадиол	male	f	18	72999	37	165	пмоль/л
346	leptin	лептин	both	f	0	1094	2.6	3.2	нг/мл
347	leptin	лептин	both	f	1095	2189	1.6	4.8	нг/мл
348	leptin	лептин	both	f	2190	3284	8.6	14.8	нг/мл
349	leptin	лептин	both	f	3285	4379	13.8	24	нг/мл
350	leptin	лептин	both	f	4380	5474	14.6	34	нг/мл
351	leptin	лептин	both	f	5475	7299	16.8	32.8	нг/мл
352	leptin	лептин	female	f	7300	72999	4.6	10	нг/мл
353	leptin	лептин	male	f	7300	72999	2.05	5.63	нг/мл
354	vitamin D	витамин Д	both	f	0	72999	30	100	нг/мл
355	vitamin D	витамин Д	both	f	0	72999	75	250	нмоль/л
356	cobalamin	витамин В12	both	f	0	6569	500	800	пг/мл
357	cobalamin	витамин В12	both	f	6570	72999	600	800	пг/мл
358	holotranscobalamin	активный В12	both	f	0	6569	85	240	пг/мл
359	holotranscobalamin	активный В12	both	f	6570	72999	95	240	пг/мл
362	folic acid in red blood cells	витамин В9 в эритроцитах	both	f	0	72999	399	640	нг/мл
371	copper in urine	медь в моче	both	f	0	72999	0	25	мкг/сут
372	copper in urine	медь в моче	both	f	0	72999	0	0.4	мкмоль/сут
363	copper in blood	медь в крови	both	f	0	2189	90	190	мкг/дл
368	copper in blood	медь в крови	female	f	4380	72999	11.8	20.45	мкмоль/л
366	copper in blood	медь в крови	both	f	2190	4379	12.58	25.17	мкмоль/л
364	copper in blood	медь в крови	both	f	0	2189	14.16	29.89	мкмоль/л
367	copper in blood	медь в крови	female	f	4380	72999	80	155	мкг/дл
365	copper in blood	медь в крови	both	f	2190	4379	80	160	мкг/дл
369	copper in blood	медь в крови	male	f	4380	72999	70	140	мкг/дл
370	copper in blood	медь в крови	male	f	4380	72999	11.01	22.03	мкмоль/л
373	ceruloplasmin	церулоплазмин	both	f	6570	72999	27	37	мг/л
374	zinc in blood	цинк в крови	both	f	30	72999	75	120	мкг/дл
375	zinc in blood	цинк в крови	both	f	30	72999	11.47	18.35	мкмоль/л
376	magnesium	магний	both	f	0	72999	0.9	1.1	ммоль/л
377	iodine in urine	йод в моче	both	f	0	72999	100	300	мкг/л
378	iodine in hair	йод в волосах	both	f	0	72999	0.25	10	мкг/г
379	potassium	калий	both	f	0	72999	3.5	5.2	ммоль/л
380	total calcium	кальций общий	both	f	0	72999	2.2	2.6	ммоль/л
381	ionized calcium	кальций ионизированный	both	f	0	72999	1.1	1.4	ммоль/л
382	phosphorus	фосфор	both	f	0	729	1.19	2.78	ммоль/л
383	phosphorus	фосфор	both	f	730	4379	1.45	1.78	ммоль/л
384	phosphorus	фосфор	both	f	4380	6569	0.87	1.45	ммоль/л
385	phosphorus	фосфор	female	f	6570	21899	0.9	1.5	ммоль/л
386	phosphorus	фосфор	female	f	21900	72999	0.9	1.32	ммоль/л
387	phosphorus	фосфор	male	f	6570	21899	0.81	1.45	ммоль/л
388	phosphorus	фосфор	male	f	21900	72999	0.75	1.2	ммоль/л
389	sodium	натрий	both	f	0	72999	136	145	ммоль/л
390	chloride	хлор	both	f	0	72999	98	106	ммоль/л
152	blood creatinine	креатинин в крови	both	f	0	4379	24	62	мкмоль/л
155	blood creatinine	креатинин в крови	male	f	6570	72999	80	115	мкмоль/л
154	blood creatinine	креатинин в крови	female	f	6570	72999	53	97	мкмоль/л
317	free cortisol urine	свободный кортизол в суточной моче	both	f	0	72999	40	70	мкг/сут
360	folic acid in blood serum	витамин В9 в сыворотке крови	both	f	0	72999	15	35	нмоль/л
361	folic acid in blood serum	витамин В9 в сыворотке крови	both	f	0	72999	6.6	15.4	нг/мл
391	vitamin B6	витамин В6	both	f	0	72999	245	400	нмоль/л
\.


--
-- Data for Name: indicator_group; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.indicator_group (id, group_name, indicators) FROM stdin;
3	биохимический анализ крови	{ферритин|мкг/л,трансферрин|г/л,"насыщение трансферрина железом|%","концентрация железа в сыворотке крови|мкмоль/л","общая железосвязывающая способность сыворотки|мкмоль/л",гомоцистеин|мкмоль/л,С-пептид|нг/мл,"С-реактивный белок|мг/л",фибриноген|г/л,"эозинофильный катионный белок|мкг/л"}
5	углеводный обмен	{"глюкоза в крови натощак|ммоль/л","гликированный гемоглобин|%","инсулин натощак|МкЕд/мл"}
6	липидограмма	{"общий холестерин|ммоль/л","липопротеины высокой плотности|ммоль/л","липопротеины низкой плотности|ммоль/л","липопротеины очень низкой плотности|ммоль/л",триглицериды|ммоль/л,"коэффициент атерогенности|ме/л"}
7	поджелудочная железа и печень	{аспартатаминотрансфераза|ме/л,аланинаминотрансфераза|ме/л,"панкреатическая амилаза|ед/л",альфа-амилаза|ед/л,"щелочная фосфатаза|ед/л",гамма-глутамилтрансфераза|ед/л}
4	белковый обмен	{"общий белок|г/л",альбумин|г/л,"креатинин в крови|мкмоль/л","креатинин в моче|г/сут",мочевина|мкмоль/л,"мочевая кислота|мкмоль/л"}
8	билирубин и его фракции	{"билирубин общий|мкмоль/л","билирубин прямой|мкмоль/л","билирубин непрямой|мкмоль/л"}
2	общий анализ крови	{гемоглобин|г/л,эритроциты|тера/л,"средний объём эритроцитов|фл","среднее содержание гемоглобина в эритроците|пг","средняя концентрация Hb в эритроцитах|г/л","относительная ширина распределения эритроцитов по объёму|%","диапазон между большим и маленьким эритроцитом|фл",гематокрит|%,тромбоциты|*10^(9)/л,"средний объём тромбоцитов|фл",лейкоциты|*10^(9)/л,нейтрофилы|%,"абсолютное количество нейтрофилов|*10^(9)/л",эозинофилы|%,"абсолютное количество эозинофилов|*10^(9)/л",базофилы|%,"абсолютное количество базофилов|*10^(9)/л",лимфоциты|%,"абсолютное количество лимфоцитов|*10^(9)/л",моноциты|%,"абсолютное количество моноцитов|*10^(9)/л","скорость оседания эритроцитов|мм/час"}
12	гормоны щитовидной железы	{"тиреотропный гормон|мМЕ/л","Т4 свободный|нг/дл|пмоль/л","Т3 свободный|пг/мл|пмоль/л","Т3 реверсивный|нг/дл","антитела к ТПО и ТГ|МЕ/мл"}
13	гормоны	{"тестостерон свободный|нмоль/л","биологически доступный тестостерон|нмоль/л","индекс свободных андрогенов|%","тестостерон общий|нмоль/л",пролактин|нг/мл,"свободный кортизол в суточной моче|мкг/сут|нмоль/сут",ДГЭА-С|мкмоль/л,прогестерон|нмоль/л|нг/дл,"глобулин, связывающий половые гормоны|нмоль/л",эстрадиол|пмоль/л,лептин|нг/мл}
14	витамины	{"витамин Д|нг/мл|нмоль/л","витамин В12|пг/мл","активный В12|пг/мл","витамин В9 в сыворотке крови|нмоль/л|нг/мл","витамин В9 в эритроцитах|нг/мл","витамин В6|нмоль/л"}
15	минералы	{"медь в крови|мкг/дл|мкмоль/л","медь в моче|мкг/сут|мкмоль/сут",церулоплазмин|мг/л,"цинк в крови|мкг/дл|мкмоль/л",магний|ммоль/л,"йод в моче|мкг/л","йод в волосах|мкг/г"}
16	электролиты	{калий|ммоль/л,"кальций общий|ммоль/л","кальций ионизированный|ммоль/л",фосфор|ммоль/л,натрий|ммоль/л,хлор|ммоль/л}
\.


--
-- Data for Name: referent; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.referent (id, current_value, reg_date, status) FROM stdin;
19	2	2022-09-22	fall
\.


--
-- Data for Name: measure; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.measure (id, person_id, indicator_id, referent_id) FROM stdin;
10	16	120	19
\.


--
-- Data for Name: raise_reason; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.raise_reason (transcript_id, reason_id) FROM stdin;
5	11
5	13
5	14
5	83
5	149
6	19
6	83
6	149
6	151
7	70
7	74
7	136
10	71
10	74
10	136
11	13
11	71
11	74
11	97
11	136
11	158
11	177
11	213
14	71
14	136
14	151
5	222
15	116
15	123
15	149
15	165
16	106
16	146
16	149
16	151
17	13
17	44
17	55
17	71
17	93
17	106
17	136
17	164
18	93
18	106
18	125
19	93
19	106
19	125
20	93
20	106
20	125
21	25
21	29
22	25
22	29
23	25
23	44
23	84
23	106
23	125
23	151
24	25
24	44
24	84
24	106
24	125
24	151
25	25
25	84
26	25
26	84
27	84
27	106
28	84
28	106
29	44
29	53
29	63
29	84
29	93
29	113
29	125
29	146
29	171
29	183
30	13
30	106
30	122
30	203
32	146
33	173
34	70
34	71
34	74
34	78
34	90
34	136
34	201
36	30
36	70
36	71
36	83
36	108
36	126
36	132
36	136
36	142
36	166
37	13
37	67
37	93
37	110
37	208
39	93
39	106
39	196
40	13
40	55
40	61
40	68
40	93
40	106
40	137
40	213
42	51
42	123
42	149
42	206
43	13
43	46
43	123
43	149
43	164
43	204
44	13
44	46
44	123
44	149
44	164
44	204
45	16
45	106
45	110
45	129
45	147
46	13
46	14
46	16
46	48
46	86
46	123
46	147
46	179
46	134
47	93
47	177
48	151
48	175
49	1
49	16
49	31
49	98
49	156
49	175
49	181
49	182
49	205
50	50
50	62
50	93
50	98
50	106
50	175
50	195
50	199
50	213
51	93
51	208
51	213
52	13
52	62
52	68
52	83
52	93
52	144
52	195
52	199
52	213
53	13
53	62
53	68
53	83
53	93
53	144
53	195
53	199
53	213
54	181
55	13
55	48
55	144
55	221
56	4
56	62
56	93
57	48
57	93
57	181
58	4
58	8
58	13
58	110
59	4
59	8
59	13
59	110
60	39
60	56
60	93
60	98
60	140
60	189
61	13
61	159
61	206
61	213
62	93
62	99
62	159
62	228
63	48
63	93
64	71
64	100
64	136
64	146
64	151
65	35
65	99
65	146
65	151
65	159
66	27
66	31
66	51
66	65
66	93
66	151
66	175
66	196
66	213
67	17
67	150
67	229
68	164
69	20
69	106
69	136
69	151
70	23
70	42
70	79
70	85
70	172
72	27
72	67
72	93
72	175
73	27
73	67
73	93
73	175
76	27
76	67
76	93
76	175
77	67
77	184
78	13
78	27
78	31
78	67
78	122
78	175
78	214
79	14
79	118
79	144
79	147
80	10
80	27
80	48
80	89
80	123
82	61
82	68
82	121
82	143
82	205
83	31
83	48
83	153
83	208
84	17
84	162
84	208
85	13
85	31
85	67
85	181
85	196
85	208
86	24
87	52
87	79
87	86
87	125
88	52
88	79
88	86
88	125
89	112
89	136
89	144
89	210
89	211
90	112
90	136
90	144
90	210
90	211
91	163
92	3
92	38
92	93
92	117
92	136
92	151
92	164
92	178
92	203
92	213
93	3
93	38
93	93
93	117
93	136
93	151
93	164
93	178
93	203
93	213
94	13
94	55
94	93
94	106
94	150
95	163
95	200
96	24
96	110
96	149
96	163
96	213
97	45
97	150
97	197
97	229
98	45
98	150
98	197
98	229
99	20
99	27
99	73
99	76
99	93
99	109
100	24
100	92
100	110
100	164
100	192
101	24
101	40
101	47
101	216
102	27
102	56
102	80
102	93
102	101
102	209
103	93
103	185
103	236
104	93
\.


--
-- Data for Name: referent_transcript; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.referent_transcript (referent_id, transcript_id) FROM stdin;
19	30
\.


--
-- Data for Name: refresh_token; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.refresh_token (id, expires_at, token, customer_id) FROM stdin;
2	2026-04-14 14:26:11.268246	e8a65aef-77f0-4cc4-aa03-087fdcbd716f	2
3	2026-04-14 14:37:23.114965	b7f85d34-ef84-4195-bbe1-b0f7518183cf	1
\.


--
-- Data for Name: unit; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.unit (id, name) FROM stdin;
1	г/л
2	тера/л
3	фл
4	пг
5	%
6	*10^(9)/л
7	мм/час
8	мкг/л
9	мкмоль/л
11	нг/мл
12	мг/л
13	ммоль/л
14	МкЕд/мл
15	ме/л
16	ед/л
17	мМЕ/л
18	нг/дл
19	пмоль/л
20	пг/мл
21	МЕ/мл
22	нмоль/л
24	мкг/дл
25	мкмоль/сут
26	мкг/сут
28	мм/ч
29	клеток/мкл
30	мкЕд/л
31	г/сут
32	ммоль/сут
41	нмоль/сут
45	мкг/г
\.


--
-- Data for Name: verification_token; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.verification_token (token, expires_at, customer_id) FROM stdin;
9b02d03c-0f83-4274-b9c3-3ce6764abaca	2026-04-07 14:18:25.315319	2
\.


--
-- Name: customer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.customer_id_seq', 6, true);


--
-- Name: indicator_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.indicator_group_id_seq', 16, true);


--
-- Name: indicator_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.indicator_id_seq', 391, true);


--
-- Name: measure_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.measure_id_seq', 10, true);


--
-- Name: person_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.person_id_seq', 16, true);


--
-- Name: reason_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.reason_id_seq', 236, true);


--
-- Name: referent_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.referent_id_seq', 19, true);


--
-- Name: refresh_token_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.refresh_token_id_seq', 3, true);


--
-- Name: transcript_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.transcript_id_seq', 104, true);


--
-- Name: unit_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.unit_id_seq', 45, true);

