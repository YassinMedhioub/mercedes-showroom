// server-groq.js
import cors from 'cors'
import 'dotenv/config'
import express from 'express'
import Groq from 'groq-sdk'

const app = express()

const ORIGIN = process.env.ORIGIN || 'http://localhost:5173'
app.use(cors({
    origin: ORIGIN,
    methods: ['GET', 'POST', 'OPTIONS'],
    allowedHeaders: ['Content-Type', 'Authorization'],
}))

app.use(express.json({ limit: '1mb' }))

// JSON parse error -> clear 400
app.use((err, req, res, next) => {
    if (err?.type === 'entity.parse.failed') {
        return res.status(400).json({ error: 'bad_json', message: err.message })
    }
    next(err)
})

app.get('/health', (_req, res) => res.json({ ok: true }))

// ---------- helpers ----------
function mkGroq() {
    if (!process.env.GROQ_API_KEY) {
        throw Object.assign(new Error('Missing GROQ_API_KEY'), { code: 'NO_KEY' })
    }
    return new Groq({ apiKey: process.env.GROQ_API_KEY })
}

function logError(tag, e) {
    // Don’t leak secrets, but show enough detail in the server console
    console.error(`[${tag}]`, e?.name || '', e?.code || '', e?.status || '', e?.message || '')
    if (e?.response) {
        console.error(`[${tag}] response status:`, e.response.status)
        console.error(`[${tag}] response data:`, e.response.data)
    }
}

// ---------- chat ----------
// server-groq.js
app.post('/api/ai/chat', async (req, res) => {
    try {
        const { messages, language = 'en' } = req.body || {}

        if (!Array.isArray(messages)) {
            return res.status(400).json({ error: 'bad_payload', message: '`messages` must be an array' })
        }
        for (const m of messages) {
            if (!m || !m.role || typeof m.content !== 'string') {
                return res.status(400).json({ error: 'bad_payload', message: 'each message needs {role, content:string}' })
            }
        }

        const systemPrompt = language === 'fr'
            ? "Tu es l'assistant Mercedes d'un showroom. Réponds utilement, clairement et brièvement."
            : "You are a Mercedes showroom assistant. Be helpful, clear, and concise."

        console.log('[api/chat] calling Groq, msgs:', messages.length)

        const client = new Groq({ apiKey: process.env.GROQ_API_KEY })

        const completion = await client.chat.completions.create({
            model: 'llama-3.1-8b-instant', // try 'llama-3.1-70b-versatile' if needed
            messages: [
                { role: 'system', content: systemPrompt },
                ...messages.map(m => ({ role: m.role, content: m.content })),
            ],
            temperature: 0.6,
            max_tokens: 400,
        })

        const reply = completion?.choices?.[0]?.message?.content?.trim() || ''
        return res.json({ reply })
    } catch (e) {
        console.error('[api/chat] ERROR name=', e?.name, 'code=', e?.code, 'status=', e?.status)
        console.error('[api/chat] message=', e?.message)
        console.error('[api/chat] response?.status=', e?.response?.status)
        console.error('[api/chat] response?.data=', e?.response?.data)

        if (!process.env.GROQ_API_KEY) {
            return res.status(500).json({ error: 'config', message: 'GROQ_API_KEY is missing' })
        }
        const s = e?.response?.status
        if (s === 401) return res.status(401).json({ error: 'unauthorized', message: 'Bad/expired API key' })
        if (s === 429) return res.status(429).json({ error: 'rate_limited', message: 'Rate limit or quota' })
        return res.status(500).json({ error: 'server_error', message: e?.message || 'unknown' })
    }
})


// ---------- describe car ----------
app.post('/api/ai/describe-car', async (req, res) => {
    try {
        const { model = '', type = '', year = '', language = 'fr' } = req.body || {}

        const sys = (language === 'fr')
            ? `Tu écris un paragraphe élégant et factuel pour la fiche modèle d’un showroom Mercedes.`
            : `You write an elegant, factual paragraph for a Mercedes showroom model page.`

        const user = (language === 'fr')
            ? `Modèle: ${model}\nType: ${type}\nAnnée: ${year}\nStyle: 3–5 phrases, ton premium, sans superlatifs excessifs, pas de prix.`
            : `Model: ${model}\nType: ${type}\nYear: ${year}\nStyle: 3–5 sentences, premium tone, avoid hype and pricing.`

        const client = mkGroq()
        const completion = await client.chat.completions.create({
            model: 'llama-3.1-8b-instant',
            messages: [
                { role: 'system', content: sys },
                { role: 'user', content: user },
            ],
            temperature: 0.6,
            max_tokens: 220,
        })

        const long = completion?.choices?.[0]?.message?.content?.trim() || ''
        return res.json({ long })
    } catch (e) {
        logError('api/ai/describe-car', e)
        if (e?.code === 'NO_KEY') return res.status(500).json({ error: 'config', message: 'GROQ_API_KEY is missing' })
        if (e?.response?.status === 401) return res.status(401).json({ error: 'unauthorized', message: 'Bad/expired API key' })
        if (e?.response?.status === 429) return res.status(429).json({ error: 'rate_limited', message: 'Rate limit or quota' })
        return res.status(500).json({ error: 'server_error', message: e?.message || 'unknown' })
    }
})

const PORT = process.env.PORT || 8787
app.listen(PORT, () => console.log(`Groq AI server on http://localhost:${PORT}`))
