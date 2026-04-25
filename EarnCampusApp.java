import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class EarnCampusApp extends JFrame {
    private final Color PRIMARY = new Color(37, 99, 235);
    private final Color BG = new Color(248, 250, 252);
    private final Color BORDER = new Color(226, 232, 240);
    private final Color TEXT_MAIN = new Color(30, 41, 59);
    private final Color TEXT_MUTED = new Color(100, 116, 139);
    private final Color GREEN = new Color(22, 163, 74);

    private String currentTab = "need";
    private final List<Post> needPosts = new ArrayList<>();
    private final List<Post> offerPosts = new ArrayList<>();

    private JPanel postFeed;
    private JTextField postTitle, postPrice;
    private JTextArea postDetails;
    private JComboBox<String> hostelSelect;
    private RoundedButton needTabBtn, offerTabBtn;
    private JLabel feedHeading;

    public EarnCampusApp() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        setTitle("EarnCampus");
        setSize(420, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        setupHeader();

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(0, 12, 20, 12));

        setupTabs(mainContent);
        setupComposer(mainContent); // Restored your original composer

        // FIXED: Heading Position
        JPanel headingWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        headingWrapper.setOpaque(false);
        headingWrapper.setMaximumSize(new Dimension(420, 40));
        feedHeading = new JLabel("I NEED POSTS");
        feedHeading.setFont(new Font("SansSerif", Font.BOLD, 12));
        feedHeading.setForeground(TEXT_MUTED);
        headingWrapper.add(feedHeading);

        mainContent.add(Box.createVerticalStrut(20));
        mainContent.add(headingWrapper);

        postFeed = new JPanel();
        postFeed.setLayout(new BoxLayout(postFeed, BoxLayout.Y_AXIS));
        postFeed.setOpaque(false);
        mainContent.add(postFeed);

        JScrollPane scroll = new JScrollPane(mainContent);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        add(scroll, BorderLayout.CENTER);

        renderPosts();
        setVisible(true);
    }

    private void setupHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(420, 80)); // Slightly taller for breathing room
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));

        // Left Side: Brand Logo
        JLabel title = new JLabel("<html><body style='padding-left: 15px;'>" +
                "<span style='font-size:22px; font-weight:800; color:#2563eb;'>Earn</span>" +
                "<span style='font-size:22px; font-weight:800; color:#1e293b;'>Campus</span>" +
                "</body></html>");

        // Right Side: Location/Hostel Container
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 22));
        rightPanel.setOpaque(false);

        // Styled Hostel Selector
        hostelSelect = new JComboBox<>(new String[] { "All Hostels", "H1", "H2", "H3" });
        hostelSelect.setFont(new Font("SansSerif", Font.BOLD, 12));
        hostelSelect.setBackground(Color.WHITE);
        hostelSelect.setFocusable(false);
        hostelSelect.setCursor(new Cursor(Cursor.HAND_CURSOR));
        hostelSelect.addActionListener(e -> renderPosts());

        // Modern Location Icon Label
        JLabel locIcon = new JLabel("📍");
        locIcon.setFont(new Font("SansSerif", Font.PLAIN, 16));

        // Add components to the right panel
        rightPanel.add(locIcon);
        rightPanel.add(hostelSelect);

        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    private void setupTabs(JPanel container) {
        JPanel bar = new RoundedPanel(12, BORDER);
        bar.setLayout(new GridLayout(1, 2, 4, 4));
        bar.setMaximumSize(new Dimension(280, 48));
        bar.setBorder(new EmptyBorder(4, 4, 4, 4));

        needTabBtn = new RoundedButton("I NEED", PRIMARY, Color.WHITE);
        offerTabBtn = new RoundedButton("I OFFER", new Color(0, 0, 0, 0), TEXT_MUTED);

        needTabBtn.addActionListener(e -> switchTab("need"));
        offerTabBtn.addActionListener(e -> switchTab("offer"));

        bar.add(needTabBtn);
        bar.add(offerTabBtn);
        container.add(Box.createVerticalStrut(20));
        container.add(bar);
    }

    private void setupComposer(JPanel container) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setMaximumSize(new Dimension(380, 280));

        postTitle = new JTextField();
        postTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        postTitle.setBorder(BorderFactory.createTitledBorder(new LineBorder(BORDER, 1, true), "TASK TITLE", 0, 0,
                new Font("SansSerif", Font.BOLD, 10), TEXT_MUTED));

        postDetails = new JTextArea(3, 20);
        postDetails.setLineWrap(true);
        postDetails.setWrapStyleWord(true);
        postDetails.setFont(new Font("SansSerif", Font.PLAIN, 13));
        postDetails.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane detailsScroll = new JScrollPane(postDetails);
        detailsScroll.setBorder(BorderFactory.createTitledBorder(new LineBorder(BORDER, 1, true), "DESCRIPTION", 0, 0,
                new Font("SansSerif", Font.BOLD, 10), TEXT_MUTED));
        detailsScroll.getViewport().setBackground(Color.WHITE);

        JPanel footer = new JPanel(new BorderLayout(15, 0));
        footer.setOpaque(false);
        footer.setMaximumSize(new Dimension(340, 50));

        JPanel pricePill = new RoundedPanel(25, BG);
        pricePill.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        pricePill.setPreferredSize(new Dimension(120, 45));
        pricePill.setBorder(new LineBorder(BORDER, 1, true));

        JLabel currencyLabel = new JLabel("₹");
        currencyLabel.setForeground(GREEN);
        currencyLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        postPrice = new JTextField("0", 5);
        postPrice.setBackground(BG);
        postPrice.setBorder(null);
        postPrice.setFont(new Font("SansSerif", Font.BOLD, 16));

        // FIX 1: Only allow numbers
        ((javax.swing.text.AbstractDocument) postPrice.getDocument())
                .setDocumentFilter(new javax.swing.text.DocumentFilter() {
                    public void replace(FilterBypass fb, int offset, int length, String text,
                            javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                        if (text.matches("\\d*"))
                            super.replace(fb, offset, length, text, attrs);
                    }
                });

        // FIX 2: Placeholder Logic (Clear '0' on click)
        postPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (postPrice.getText().equals("0"))
                    postPrice.setText("");
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (postPrice.getText().isEmpty())
                    postPrice.setText("0");
            }
        });

        pricePill.add(currencyLabel);
        pricePill.add(postPrice);

        RoundedButton postBtn = new RoundedButton("Post Request", PRIMARY, Color.WHITE);
        postBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        postBtn.setPreferredSize(new Dimension(150, 45));
        postBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        postBtn.addActionListener(e -> handlePost());

        footer.add(pricePill, BorderLayout.WEST);
        footer.add(postBtn, BorderLayout.CENTER);

        card.add(postTitle);
        card.add(Box.createVerticalStrut(12));
        card.add(detailsScroll);
        card.add(Box.createVerticalStrut(15));
        card.add(footer);

        container.add(Box.createVerticalStrut(10));
        container.add(card);
    }

    private void handlePost() {
        if (postTitle.getText().trim().isEmpty())
            return;
        Post p = new Post(postTitle.getText(), postDetails.getText(), postPrice.getText(),
                (String) hostelSelect.getSelectedItem());
        if (currentTab.equals("need"))
            needPosts.add(0, p);
        else
            offerPosts.add(0, p);
        postTitle.setText("");
        postDetails.setText("");
        postPrice.setText("0");
        renderPosts();
    }

    private void renderPosts() {
        postFeed.removeAll();
        List<Post> active = currentTab.equals("need") ? needPosts : offerPosts;
        String selH = (String) hostelSelect.getSelectedItem();

        for (Post p : active) {
            if (!selH.equals("All Hostels") && !p.hostel.equals(selH))
                continue;

            RoundedPanel card = new RoundedPanel(20, Color.WHITE);
            card.setLayout(new BorderLayout());
            card.setBorder(new EmptyBorder(15, 18, 18, 18));
            card.setMaximumSize(new Dimension(390, 240));

            JPanel cardHeader = new JPanel(new BorderLayout());
            cardHeader.setOpaque(false);

            JPanel userMeta = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            userMeta.setOpaque(false);
            JLabel avatar = new JLabel("👤");
            avatar.setFont(new Font("SansSerif", Font.PLAIN, 20));

            JPanel titleStack = new JPanel(new GridLayout(2, 1, 0, 0));
            titleStack.setOpaque(false);

            // FIXED: Added HTML wrapping to the title to prevent overflow
            JLabel title = new JLabel(
                    "<html><div style='width: 180px; font-family: SansSerif; font-weight: bold; font-size: 11px;'>"
                            + p.title + "</div></html>");

            JLabel hostelTag = new JLabel("📍 " + p.hostel);
            hostelTag.setFont(new Font("SansSerif", Font.PLAIN, 11));
            hostelTag.setForeground(TEXT_MUTED);
            titleStack.add(title);
            titleStack.add(hostelTag);

            userMeta.add(avatar);
            userMeta.add(titleStack);

            RoundedPanel priceBadge = new RoundedPanel(12, new Color(239, 246, 255));
            priceBadge.setLayout(new GridBagLayout());
            priceBadge.setPreferredSize(new Dimension(70, 35));
            JLabel priceLabel = new JLabel("₹" + p.price);
            priceLabel.setForeground(PRIMARY);
            priceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            priceBadge.add(priceLabel);

            cardHeader.add(userMeta, BorderLayout.WEST);
            cardHeader.add(priceBadge, BorderLayout.EAST);

            JLabel det = new JLabel(
                    "<html><body style='width: 250px; margin-top: 10px; color: #475569; font-family: SansSerif; font-size: 11px;'>"
                            + p.details + "</body></html>");

            JPanel actionPanel = new JPanel();
            actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
            actionPanel.setOpaque(false);

            if (currentTab.equals("need") && p.selectedUser == null) {
                if (!p.showInput) {
                    RoundedButton help = new RoundedButton("Offer Help", PRIMARY, Color.WHITE);
                    help.setPreferredSize(new Dimension(350, 40));
                    help.addActionListener(e -> {
                        p.showInput = true;
                        renderPosts();
                    });
                    actionPanel.add(Box.createVerticalStrut(15));
                    actionPanel.add(help);
                } else {
                    JTextField nameIn = new JTextField();
                    nameIn.setBorder(BorderFactory.createTitledBorder(new LineBorder(BORDER), "ENTER YOUR NAME", 0, 0,
                            new Font("SansSerif", Font.BOLD, 9)));
                    RoundedButton sub = new RoundedButton("Confirm Offer", GREEN, Color.WHITE);
                    sub.addActionListener(e -> {
                        String inputName = nameIn.getText().trim();
                        if (inputName.isEmpty())
                            return;

                        if (p.interestedUsers.contains(inputName)) {
                            JOptionPane.showMessageDialog(this, "You have already shown interest!", "Error",
                                    JOptionPane.WARNING_MESSAGE);
                        } else {
                            p.interestedUsers.add(inputName);
                            p.showInput = false;
                            renderPosts();
                        }
                    });
                    actionPanel.add(Box.createVerticalStrut(10));
                    actionPanel.add(nameIn);
                    actionPanel.add(Box.createVerticalStrut(8));
                    actionPanel.add(sub);
                }
            }

            if (!p.interestedUsers.isEmpty() && p.selectedUser == null) {
                actionPanel.add(Box.createVerticalStrut(15));
                for (String user : p.interestedUsers) {
                    JPanel row = new JPanel(new BorderLayout());
                    row.setOpaque(false);
                    JLabel uLabel = new JLabel("✋ " + user + " offered help");
                    uLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
                    row.add(uLabel, BorderLayout.WEST);

                    if (currentTab.equals("need")) {
                        JButton acc = new RoundedButton("Accept", GREEN, Color.WHITE);
                        acc.setPreferredSize(new Dimension(80, 28));
                        acc.addActionListener(e -> {
                            p.selectedUser = user;
                            renderPosts();
                        });
                        row.add(acc, BorderLayout.EAST);
                    }
                    actionPanel.add(row);
                    actionPanel.add(Box.createVerticalStrut(5));
                }
            }

            if (p.selectedUser != null) {
                JPanel statusBox = new RoundedPanel(8, new Color(240, 253, 244));
                statusBox.setLayout(new FlowLayout(FlowLayout.CENTER));
                JLabel status = new JLabel("✅ Assigned to " + p.selectedUser);
                status.setForeground(GREEN);
                status.setFont(new Font("SansSerif", Font.BOLD, 12));
                statusBox.add(status);
                actionPanel.add(Box.createVerticalStrut(15));
                actionPanel.add(statusBox);
            }

            JPanel centerContent = new JPanel(new BorderLayout());
            centerContent.setOpaque(false);
            centerContent.add(det, BorderLayout.NORTH);
            centerContent.add(actionPanel, BorderLayout.CENTER);

            card.add(cardHeader, BorderLayout.NORTH);
            card.add(centerContent, BorderLayout.CENTER);

            postFeed.add(card);
            postFeed.add(Box.createVerticalStrut(15));
        }
        postFeed.revalidate();
        postFeed.repaint();
    }

    private void switchTab(String tab) {
        this.currentTab = tab;
        feedHeading.setText(tab.equals("need") ? "I NEED POSTS" : "I OFFER POSTS");

        if (tab.equals("need")) {
            needTabBtn.setColors(PRIMARY, Color.WHITE);
            offerTabBtn.setColors(new Color(0, 0, 0, 0), TEXT_MUTED);
        } else {
            offerTabBtn.setColors(PRIMARY, Color.WHITE);
            needTabBtn.setColors(new Color(0, 0, 0, 0), TEXT_MUTED);
        }
        renderPosts();
    }

    class RoundedPanel extends JPanel {
        private int r;
        Color c;

        RoundedPanel(int r, Color c) {
            this.r = r;
            this.c = c;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), r, r));
            g2.dispose();
        }
    }

    class RoundedButton extends JButton {
        private Color bg, fg;

        RoundedButton(String t, Color b, Color f) {
            super(t);
            this.bg = b;
            this.fg = f;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(f);
        }

        public void setColors(Color b, Color f) {
            this.bg = b;
            this.fg = f;
            setForeground(f);
            repaint();
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    static class Post {
        String title, details, price, hostel, selectedUser;
        List<String> interestedUsers = new ArrayList<>();
        boolean showInput = false;

        Post(String t, String d, String p, String h) {
            title = t;
            details = d;
            price = p;
            hostel = h;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EarnCampusApp::new);
    }
}
