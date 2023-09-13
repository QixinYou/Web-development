const express = require('express');
const path = require('path');
const app = express();


const apiRoutes = require('./app');

const distPath = path.join(__dirname, 'dist');
app.use(express.static(distPath));

app.use('/api', apiRoutes);

app.get('/*', (req, res) => {
    res.sendFile(path.join(distPath, 'index.html'));
});


const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
    console.log(`App listening on port ${PORT}`);
    console.log('Press Ctrl+C to quit.');
});
// [END gae_node_request_example]
