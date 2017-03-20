/* Widget minimize */

  $('.iminimize').click(function(e){
    e.preventDefault();
    var $wcontent = $(this).parent().parent().parent().next('.invoiceItem');
    if($wcontent.is(':visible')) 
    {
      $(this).children('i').removeClass('fa-chevron-up');
      $(this).children('i').addClass('fa-chevron-down');
    }
    else 
    {
      $(this).children('i').removeClass('fa-chevron-down');
      $(this).children('i').addClass('fa-chevron-up');
    }            
    $wcontent.toggle(500);
    
    console.log();
    
  }); 
  
