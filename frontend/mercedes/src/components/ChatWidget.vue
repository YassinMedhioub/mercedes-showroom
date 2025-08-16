<template>
  <!-- FAB -->
  <!-- FAB -->
<button
  @click="open = !open"
  class="fixed bottom-4 right-4 z-[1000] rounded-full p-3 shadow-xl transition
         hover:scale-105 focus:outline-none"
  :class="isDark ? 'bg-zinc-800 text-white' : 'bg-white border border-zinc-300 text-zinc-800'"
  aria-label="Open chat"
>
  <!-- Replace the emoji with the logo image -->
  <img :src="logo" alt="Mercedes" class="w-6 h-6 sm:w-7 sm:h-7 object-contain" />
</button>

  <!-- Panel -->
  <div v-if="open"
       class="fixed bottom-20 right-4 z-[1000] w-80 sm:w-96 rounded-2xl overflow-hidden shadow-2xl flex flex-col"
       :class="isDark ? 'bg-zinc-900 text-white' : 'bg-white text-zinc-800 border border-zinc-200'">
    <!-- Header -->
    <div class="px-4 py-3 flex items-center justify-between" :class="isDark ? 'bg-zinc-900' : 'bg-zinc-50'">
      <div class="font-semibold text-sm sm:text-base">Assistant Mercedes</div>
      <button @click="open=false" class="text-xs opacity-70 hover:opacity-100">✕</button>
    </div>

    <!-- Messages -->
    <div ref="scroll" class="px-3 py-3 h-72 overflow-y-auto space-y-3">
      <div v-for="(m, i) in messages" :key="i" class="flex" :class="m.role==='user' ? 'justify-end' : 'justify-start'">
        <div class="max-w-[85%] px-3 py-2 rounded-xl text-sm leading-relaxed"
             :class="m.role==='user'
               ? (isDark ? 'bg-zinc-700 text-white' : 'bg-zinc-200 text-zinc-900')
               : (isDark ? 'bg-zinc-800 text-zinc-100' : 'bg-white border border-zinc-200')">
          <div v-html="m.html || escape(m.text)"></div>
        </div>
      </div>

      <!-- typing bubble -->
      <div v-if="loading" class="flex justify-start">
        <div class="px-3 py-2 rounded-xl text-sm"
             :class="isDark ? 'bg-zinc-800 text-zinc-100' : 'bg-white border border-zinc-200'">
          <span class="opacity-70">…</span>
        </div>
      </div>
    </div>

    <!-- Quick chips -->
    <div class="px-3 pb-2 flex flex-wrap gap-2">
      <button v-for="q in quick" :key="q.label" @click="send(q.prompt)"
              class="text-xs px-2 py-1 rounded-full border transition hover:scale-105"
              :class="isDark ? 'border-zinc-700 hover:bg-zinc-800' : 'border-zinc-300 hover:bg-zinc-100'">
        {{ q.label }}
      </button>
    </div>

    <!-- Input -->
    <form @submit.prevent="onSubmit" class="p-3 border-t" :class="isDark ? 'border-zinc-800' : 'border-zinc-200'">
      <div class="flex items-center gap-2">
        <input v-model="draft" type="text" :placeholder="t('placeholder')"
               class="flex-1 px-3 py-2 rounded-lg text-sm focus:outline-none"
               :class="isDark ? 'bg-zinc-800 text-white placeholder:text-zinc-400' : 'bg-white border border-zinc-300'"/>
        <button type="submit"
                class="px-3 py-2 rounded-lg text-sm font-medium transition"
                :class="isDark ? 'bg-[#5d737e] text-white hover:bg-zinc-700' : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'">
          {{ t('send') }}
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { computed, nextTick, onMounted, ref, watch } from "vue";
import logoBlack from '/src/assets/mercedes-logo-black.png';
import logoWhite from '/src/assets/mercedes-logo.png';

/* Props */
const props = defineProps({
  language: { type: String, default: "FR" },
  isDark: { type: Boolean, default: false },
  cars: { type: Array, default: null },
});
const logo = computed(() => (props.isDark ? logoWhite : logoBlack))
const emit = defineEmits(["navigate"]);

/* Local car DB fallback (use yours if already available) */
import aClassAmgLine from "/src/assets/cars/AClassAmgLine.png";
import amgGt from "/src/assets/cars/amg_gt.png";
import c200 from "/src/assets/cars/C200.png";
import gla200d from "/src/assets/cars/GLA200D.png";
import gleS63 from "/src/assets/cars/GLES63.avif";
import sClass2012 from "/src/assets/cars/Sclass2012.png";

const localCars = [
  { id:1, model:"Classe S 2012",            family:"Classe S", type:"Berline de Luxe", price:"110 900", image:sClass2012, aliases:["classe s","class s","s-class","w221","mercedes s"] },
  { id:2, model:"Classe A 200 d AMG Line",  family:"Classe A", type:"Citadine Compacte", price:"159 200", image:aClassAmgLine, aliases:["classe a","class a","a-class","a 200","a200"] },
  { id:3, model:"Mercedes Benz GLE S 63 4MATIC Coupé", family:"GLE Coupé", type:"SUV", price:"240 500", image:gleS63, aliases:["gle","gle coupe","gle coupé","gle s63"] },
  { id:4, model:"Mercedes AMG GT",          family:"AMG GT",  type:"Sports Car", price:"170 000", image:amgGt, aliases:["amg gt","gt amg"] },
  { id:5, model:"Classe C 200 Avantgarde",  family:"Classe C", type:"Break", price:"110 000", image:c200, aliases:["classe c","class c","c-class","c200"] },
  { id:6, model:"GLA 200 D",                family:"GLA",      type:"SUV Coupé", price:"92 400",  image:gla200d, aliases:["gla","gla 200","gla200"] },
];
const cars = computed(() => props.cars?.length ? props.cars : localCars);

/* UI state */
const open = ref(false);
const draft = ref("");
const messages = ref([]);
const loading = ref(false);
const scroll = ref(null);

/* i18n */
const dict = {
  FR: {
    hello: "Bonjour ! Je suis votre assistant Mercedes. Posez-moi vos questions.",
    placeholder: "Demandez un modèle, une comparaison, etc.",
    send: "Envoyer",
  },
  EN: {
    hello: "Hi! I’m your Mercedes assistant. Ask me anything.",
    placeholder: "Ask about a model, comparison, etc.",
    send: "Send",
  },
};
const t = (k) => dict[props.language]?.[k] ?? dict.FR[k];

/* helpers */
const escape = (s) => (s||"").replaceAll("<","&lt;").replaceAll(">","&gt;");
const norm = (s) => (s||"").toLowerCase().normalize("NFD").replace(/\p{Diacritic}/gu,"")
  .replace(/[^a-z0-9\s\-]/g," ").replace(/\s+/g," ").trim();

function add(role, payload) {
  const msg = typeof payload === "string" ? { text: payload } : payload;
  messages.value.push({ role, text: msg.text || "", html: msg.html || null });
  nextTick(() => { if (scroll.value) scroll.value.scrollTop = scroll.value.scrollHeight; });
}

/* quick suggestions */
const quick = computed(() =>
  props.language === "FR"
    ? [
        { label: "Infos Classe A", prompt: "Donne-moi des informations sur la Classe A" },
        { label: "Avis Classe S",  prompt: "Que penses-tu de la Classe S ?" },
        { label: "Comparer A/C",   prompt: "Compare la Classe A et la Classe C" },
      ]
    : [
        { label: "Info A-Class",    prompt: "Give me information about the A-Class" },
        { label: "Opinion S-Class", prompt: "What do you think about the S-Class?" },
        { label: "Compare A/C",     prompt: "Compare A-Class and C-Class" },
      ]
);

/* -------- AI integration (Groq proxy) -------- */
const AI_URL = import.meta.env.VITE_AI_URL || "http://localhost:8787/api/chat";

// Provide lightweight domain context from local data
function buildContext(question) {
  const q = norm(question);
  const hit = cars.value.filter(c => {
    const all = [c.model, c.family, ...(c.aliases || [])].map(norm).join(" ");
    return q.split(" ").some(w => w.length >= 2 && all.includes(w));
  });
  const picked = hit.length ? hit : cars.value; // if nothing matched, give a tiny catalog snapshot

  const lines = picked.slice(0, 6).map(c =>
    `- ${c.model} (${c.family}) — ${c.type} — ${c.price} DT`
  ).join("\n");
  return `Cars in catalog:\n${lines}\n(Use only if relevant.)`;
}

// inside <script setup> of ChatWidget.vue

async function askLLM(userText) {
  // Build minimal chat history for the server
  const history = messages.value.map(m => ({
    role: m.role === 'assistant' ? 'assistant' : 'user',
    content: m.text || m.content || ''
  }))
  // Push the latest user message
  history.push({ role: 'user', content: userText })

  const lang = (props.language || 'FR').toUpperCase() === 'FR' ? 'fr' : 'en'

  // IMPORTANT: use the relative path so Vite forwards to 8787
  const res = await fetch('/api/chat', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      language: lang,         // 'fr' or 'en'
      messages: history       // [{role:'user'|'assistant', content:'...'}, ...]
    }),
  })

  if (!res.ok) {
    // Try to read server error payload to understand 400s
    let detail = ''
    try { detail = JSON.stringify(await res.json()) } catch {}
    throw new Error(`AI proxy error: ${res.status} ${detail}`)
  }

  const data = await res.json()
  return data.reply || ''
}



/* send */
async function send(text) {
  add('user', text)
  try {
    const reply = await askLLM(text)
    add('assistant', reply)
  } catch (e) {
    console.error(e)
    add('assistant', (props.language === 'FR'
      ? "Le service est momentanément indisponible."
      : "The service is temporarily unavailable."))
  }
}

function onSubmit() {
  const text = draft.value.trim();
  if (!text) return;
  draft.value = "";
  send(text);
}

/* lifecycle */
onMounted(() => { if (!messages.value.length) add("assistant", t("hello")); });
watch(() => props.language, () => { if (!messages.value.length) add("assistant", t("hello")); });
</script>
