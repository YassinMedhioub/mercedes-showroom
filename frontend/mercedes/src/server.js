import cors from 'cors';
import { config } from 'dotenv';
import express from 'express';
import OpenAI from 'openai';

config();

const app = express();
app.use(express.json({ limit: '1mb' }));
app.use(cors());

if (!process.env.OPENAI_API_KEY) {
    console.error('❌ Missing OPENAI_API_KEY. Add it to .env next to server.js');
    process.exit(1);
}

const client = new OpenAI({ apiKey: process.env.OPENAI_API_KEY });

app.get('/health', (req, res) => {
    res.json({
        ok: true,
        hasKey: !!process.env.OPENAI_API_KEY,
        model: process.env.OPENAI_MODEL || 'gpt-4o-mini'
    });
});

app.post('/api/ai/describe-car', async (req, res) => {
    try {
        const { model, type, year, language = 'fr' } = req.body || {};
        if (!model || !type) return res.status(400).json({ error: 'model and type are required' });

        const langName = language === 'fr' ? 'French' : 'English';
        const prompt = `
You are a Mercedes-Benz product copywriter.
Write one elegant paragraph (80–120 words) describing this car for a showroom website.
Keep it factual, refined, and classy. Avoid pushy marketing clichés or price.
Car: ${model}${year ? ` (${year})` : ''}, ${type}.
Write in ${langName}.
`.trim();

        const modelName = process.env.OPENAI_MODEL || 'gpt-4o-mini';

        const cc = await client.chat.completions.create({
            model: modelName,
            temperature: 0.7,
            messages: [
                { role: 'system', content: 'You are a helpful, precise automotive copywriter.' },
                { role: 'user', content: prompt }
            ]
        });

        const text = cc.choices?.[0]?.message?.content?.trim() || '';
        if (!text) throw new Error('No text returned from model');

        res.json({ long: text });
    } catch (err) {
        console.error('AI error:', err?.status || '', err?.message || '', err?.response?.data || '');
        res.status(500).json({
            error: 'ai_failed',
            message: err?.response?.data?.error?.message || err?.message || 'Unknown error'
        });
    }
});


// API Endpoint to Get Events
app.get('/api/events', async (req, res) => {
    try {
        const [rows] = await pool.query('SELECT * FROM events');
        res.json(rows);
    } catch (err) {
        console.error('Error fetching events:', err);
        res.status(500).json({ error: 'Failed to fetch events' });
    }
});

// Serve Static Images (Optional)
app.use('/images', express.static('public/images'));

// Car Management
export async function fetchCars(params = {}) {
    try {
        const response = await api.get('/cars', { params })
        return response.data
    } catch (error) {
        console.error('Failed to fetch cars:', error)
        throw error
    }
}


const PORT = process.env.PORT || 8787;
app.listen(PORT, () => {
    console.log(`✅ AI server running on http://localhost:${PORT}`);
});