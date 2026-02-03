document.addEventListener('DOMContentLoaded', function() {
  var navLinks = document.querySelectorAll('.nav-link');
  var ctaButton = document.querySelector('.cta-button');

  function smoothScroll(e) {
    e.preventDefault();
    var targetId = this.getAttribute('href');
    
    if (targetId.startsWith('#')) {
      var targetSection = document.querySelector(targetId);
      
      if (targetSection) {
        var navbarHeight = document.querySelector('.navbar').offsetHeight;
        var targetPosition = targetSection.offsetTop - navbarHeight;
        
        window.scrollTo({
          top: targetPosition,
          behavior: 'smooth'
        });
      }
    }
  }

  navLinks.forEach(function(link) {
    link.addEventListener('click', smoothScroll);
  });
  
  if (ctaButton) {
    ctaButton.addEventListener('click', smoothScroll);
  }

  console.log('Portfolio loaded successfully!');
});
