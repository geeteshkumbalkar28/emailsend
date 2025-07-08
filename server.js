const express = require('express');
const cors = require('cors');
const nodemailer = require('nodemailer');

const app = express();
const PORT = process.env.PORT || 5000;

app.use(cors());
app.use(express.json());

// POST /api/contact
app.post('/api/contact', async (req, res) => {
  const { name, email, phone, message } = req.body;

  if (!name || !email || !phone || !message) {
    return res.status(400).json({ success: false, message: 'All fields are required.' });
  }

  try {
    const transporter = nodemailer.createTransport({
      host: 'mail.alphaseam.com',
      port: 465,
      secure: true, // use SSL
      auth: {
        user: 'info.sairuraldevelopmenttrust@alphaseam.com',
        pass: 'MyP@ssward123', // ⚠️ Keep this secure in production!
      },
    });

    const mailOptions = {
      from: `"SRDT Website Contact" <info.sairuraldevelopmenttrust@alphaseam.com>`,
      to: 'info.sairuraldevelopmenttrust@alphaseam.com', // send to self
      subject: 'New Contact Form Submission',
      html: `
        <h2>New Contact Message from sairuraldevelopmenttrust</h2>
        <p><strong>Name:</strong> ${name}</p>
        <p><strong>Email:</strong> ${email}</p>
        <p><strong>Phone:</strong> ${phone}</p>
        <p><strong>Message:</strong><br/>${message}</p>
      `,
    };

    await transporter.sendMail(mailOptions);

    res.status(200).json({ success: true, message: 'Email sent successfully.' });
  } catch (error) {
    console.error('SendMail Error:', error);
    res.status(500).json({ success: false, message: 'Failed to send email.' });
  }
});

app.listen(PORT, () => {
  console.log(`✅ Server running at http://localhost:${PORT}`);
});
