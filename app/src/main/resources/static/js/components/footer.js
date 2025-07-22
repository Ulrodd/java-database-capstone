function renderFooter() {
  const footer = document.getElementById("footer");
  if (!footer) return;

  footer.innerHTML = `
    <footer class="footer">
      <div class="footer-logo">
        <img src="../assets/images/logo/logo.png" alt="Logo" />
        <p>© Copyright 2025</p>
      </div>
      <div class="footer-columns">
        <div class="footer-column">
          <h4>Entreprise</h4>
          <a href="/about.html">À propos</a>
          <a href="/careers.html">Carrières</a>
          <a href="/press.html">Presse</a>
        </div>
        <div class="footer-column">
          <h4>Support</h4>
          <a href="/account.html">Compte</a>
          <a href="/help-center.html">Centre d’aide</a>
          <a href="/contact.html">Contact</a>
        </div>
        <div class="footer-column">
          <h4>Légaux</h4>
          <a href="/terms.html">Conditions</a>
          <a href="/privacy.html">Politique de confidentialité</a>
          <a href="/licenses.html">Licences</a>
        </div>
      </div>
    </footer>
  `;
}

renderFooter();

// Export si besoin
export { renderFooter };
