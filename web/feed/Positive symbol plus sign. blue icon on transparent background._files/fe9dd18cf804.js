;(function(global,$){var Zoom={};Zoom.VERSION='2.0.0';Zoom.LOGGER=log.getLogger('zoom');Zoom.Scales=function(options){this.initialize.apply(this,arguments);this.idx=0;};Zoom.Scales.prototype=Array.prototype;_.extend(Zoom.Scales.prototype,Backbone.Events,{initialize:function(options){this.listenTo(options.view,'reset',this.reset);var prev_scale=1,long_side=Math.max(options.width,options.height);_.each(options.sizes,function(size){var scale=(size.size/long_side);this.push({adjust:(scale/prev_scale),display:size.name,scale:scale});prev_scale=scale;},this);},current:function(){return this[this.idx-1];},next:function(){return this[this.idx];},params:function(){return{scale:this.next().scale};},increment:function(){this.idx++;},reset:function(){this.idx=0;},hasNext:function(){return(this.idx<this.length);}});Zoom.Box=Backbone.View.extend({tagName:'div',className:'zoom-box',offset:{left:0,top:0},initialize:function(options){this.view=options.view;this.view.$el.on('mousemove',_.throttle(_.bind(this.mousemove,this),25));this.view.$el.on('mouseenter',_.bind(this.mouseenter,this));this.view.$el.on('mouseleave',_.bind(this.mouseleave,this));this.listenTo(this.view,'reset',this.reset,this);this.listenTo(this.view,'loading',this.loading,this);this.listenTo(this.view,'load',this.load,this);this.offset.left=this.offset.top=0;this.setup();},setup:function(){var scale=this.view.scales.next();this.$el.width(Math.floor(this.view.width/scale.adjust));this.$el.height(Math.floor(this.view.height/scale.adjust));this.data={max_left:(this.view.width-this.$el.outerWidth()),max_top:(this.view.height-this.$el.outerHeight()),center_left:(this.$el.width()/2),center_top:(this.$el.height()/2)};if(this.view.$el.is(':hover'))this.mouseenter();},mousemove:function(event){var offset=this.view.$el.offset();var mouseLeft=Math.floor(event.clientX-offset.left+$(document).scrollLeft()),mouseTop=Math.floor(event.clientY-offset.top+$(document).scrollTop());this.$el.css({left:Math.max(Math.min((mouseLeft-this.data.center_left),this.data.max_left),0),top:Math.max(Math.min((mouseTop-this.data.center_top),this.data.max_top),0)});},mouseenter:function(event){if(this.view.scales.hasNext()){this.$el.show();}},mouseleave:function(event){this.$el.hide();},params:function(){return{left:this.offset.left,top:this.offset.top};},loading:function(event){var position=this.$el.position();var scale=this.view.scales.next();this.offset.left=parseInt((this.offset.left+position.left)*scale.adjust);this.offset.top=parseInt((this.offset.top+position.top)*scale.adjust);this.$el.hide();},load:function(event){if(this.view.scales.hasNext()){if(this.view.$el.is(':hover'))this.$el.show();this.setup();}},reset:function(event){this.offset.left=this.offset.top=0;this.setup();}});Zoom.Details=Backbone.View.extend({tagName:'div',className:'zoom-details',initialize:function(options){this.view=options.view;this.listenTo(this.view,'reset',this.reset);this.listenTo(this.view,'load',this.load);},reset:function(){this.$el.hide();},load:function(){var scale=this.view.scales.current();this.$el.html(scale.display).show();}});Zoom.Loading=Backbone.View.extend({tagName:'div',className:'zoom-loading',initialize:function(options){this.view=options.view;this.listenTo(this.view,'load reset',this.load);this.listenTo(this.view,'loading',this.loading);},loading:function(event){this.$el.show();},load:function(){this.$el.hide();}});Zoom.Image=Backbone.View.extend({tagName:'img',className:'zoom-img',events:{'error':'error','load':'load'},initialize:function(options){this.view=options.view;this.listenTo(this.view,'reset',this.reset);},reset:function(event){this.$el.hide();},error:function(event){this.trigger('error');},load:function(event){this.view.trigger('load');this.$el.show();}});Zoom.View=Backbone.View.extend({defaults:{message:'Zoom Error',url:'/zoom/'},events:{'mouseup':'mouseup'},initialize:function(options){_.bindAll(this,'setup','reset');_.defaults(this,options,this.defaults);$(document).on('mouseup',this.reset);this.$('img').each(_.bind(function(idx,el){if(el.complete)this.setup();},this)).on('load',this.setup);},setup:function(){this.width=this.$el.width();this.height=this.$el.height();Zoom.LOGGER.info('Zoom Width:'+this.width+' Zoom Height:'+this.height);this.scales=new Zoom.Scales({height:this.height,width:this.width,sizes:this.sizes,view:this});this.box_view=new Zoom.Box({view:this});this.$el.append(this.box_view.$el);this.image_view=new Zoom.Image({view:this});this.$el.append(this.image_view.$el);this.details_view=new Zoom.Details({view:this});this.$el.append(this.details_view.$el);this.loading_view=new Zoom.Loading({view:this});this.$el.append(this.loading_view.$el);this.listenTo(this.image_view,'error',this.error);},mouseup:function(event){event.stopPropagation();if(this.scales.hasNext()){this.zoom(event);}else{this.reset(event);}},params:function(){return _.extend({height:this.height,width:this.width,id:this.id,_ts:_.now()},this.scales.params(),this.box_view.params());},zoom:function(event){this.trigger('loading');var params=this.params();Zoom.LOGGER.info('Zoom Params:',params);this.image_view.$el.attr('src',this.url+'?'+$.param(params));this.scales.increment();},reset:function(event){this.trigger('reset');},error:function(){Zoom.LOGGER.info('Zoom Error:',arguments);this.reset();this.details_view.remove();this.loading_view.remove();this.image_view.remove();this.box_view.remove();this.undelegateEvents();this.$el.append('<div class="zoom-message">'+'<div class="zoom-error">'+'<p>'+this.message+'</p>'+'</div>'+'</div>');$(document).on('mouseup',_.bind(function(){this.$('div.zoom-message').remove();},this));}});global.Zoom=Zoom;$.fn.zoom=function(options){return this.each(function(){if(!$(this).data('zoom')){$(this).data('zoom',new Zoom.View(_.extend({el:$(this)},options)));}});};}(this,jQuery));;(function(csp,$,undefined){csp.DownloadRequestModel=Backbone.Model.extend({xhr:null,defaults:{is_locked:false,is_ready:false,is_slow:false,attribution:'',downloads:[],address:'',send:false,url:null},is_polling:false,initialize:function(attributes,options){this.on('sync',_.bind(this.polling,this));},start_polling:function(){this.is_polling=true;this.fetch();},stop_polling:function(){this.is_polling=false;if(this.xhr)this.xhr.abort();},polling:function(model,resp,options){if(!this.get('is_locked')&&!this.get('is_ready')&&this.is_polling){_.delay(_.bind(this.fetch,this),1000);}},sync:function(method,model,options){if(this.xhr)this.xhr.abort();_.defaults(options||(options={}),{contentType:'application/json; charset=UTF-8',data:JSON.stringify(model),method:'POST',headers:{'X-CSRFToken':Cookies.get('csrftoken')},url:jslink({viewname:'download:api'})});this.xhr=$.ajax(options);return this.xhr;},email:function(address){$.ajax({url:jslink({viewname:'download:email'}),data:{hash:this.get('hash'),address:address}});},footer_text:function(){var text=gettext('Your download should start automatically, if it does not please click %(link_start)shere%(link_end)s.');return interpolate(text,{link_start:'<a href="'+this.get('url')+'">',link_end:'</a>'},true);},support_text:function(){var text=gettext('Please contact %(link_start)ssupport%(link_end)s if this problem persists.');return interpolate(text,{link_start:'<a href="/support.php?md12=true">',link_end:'</a>'},true);}});var EmailDownloadRequestView=Backbone.View.extend({tagName:'div',events:{'click span.btn':'submit'},template:_.template('<strong class="text-strong"><%= title %></strong>'+'<% if (!send) { %>'+'<div class="input-group">'+'<input class="form-control" name="address" type="email" value="<%= address %>" placeholder="<%= placeholder %>" />'+'<span class="input-group-btn">'+'<span class="btn btn-default">'+'<i class="cspicon icon-envelope"></i>'+'<span>'+'&nbsp;&nbsp;'+
gettext('Email me')+'</span>'+'</span>'+'</span>'+'</div>'+'<% } else { %>'+'<p class="text-success">'+
gettext('Sent! Please check your email in a couple minutes')+'</p>'+'<% } %>'),initialize:function(options){this.listenTo(this.model,'change:address change:send',this.render);this.render();},render:function(){var html=this.template({placeholder:gettext('Email Address'),address:this.model.get('address'),send:this.model.get('send'),title:ngettext('Email me this file','Email me these files',this.model.get('downloads').length)});this.$el.html(html);return this;},submit:function(){var address=this.$('input[name="address"]').val();this.model.set('send',true);this.model.email(address);}});csp.DownloadRequestView=Backbone.View.extend({tagName:'div',className:'download-modal modal fade',events:{'hidden.bs.modal':'hide'},template:_.template('<div class="modal-dialog">'+'<div class="modal-content">'+'<div class="modal-header">'+'<button type="button" class="close" data-dismiss="modal">'+'<span>&times;</span>'+'</button>'+'<h4 class="modal-title">'+gettext('Thank you for your download')+'</h4>'+'</div>'+'<div class="modal-body">'+'<% if (request.get("is_locked")) { %>'+'<div class="alert alert-danger text-center">'+'<strong>'+
gettext('We are having trouble with your last request.')+'</strong>'+'<br/>'+'<%= request.support_text() %>'+'</div>'+'<% } else { %>'+'<ul class="list-inline text-center">'+'<% _.each(request.get("downloads"), function (download) { %>'+'<li class="img-thumbnail<% print(download.is_ready ? \'\' : \' loading\') %>">'+'<img src="<%= download.src %>" />'+'</li>'+'<% }); %>'+'</ul>'+'<% if (!request.get("is_ready")) { %>'+'<h3 class="text-center">'+
gettext("We're creating your download.")+'<% if (request.get("is_slow")) { %>'+' '+gettext('It will start in a few minutes.')+'<% } %>'+'</h3>'+'<p class="text-center">'+
gettext("Don't want to wait?")+' '+
gettext("No problem, we can email you as soon as it's ready.")+'</p>'+'<% } else { %>'+'<%= request.get("attribution") %>'+'<% } %>'+'<% } %>'+'</div>'+'<% if (!request.get("is_locked")) { %>'+'<div class="modal-footer">'+'<% if (request.get("is_ready")) { %>'+'<%= request.footer_text() %>'+'<% } else { %>'+'<span class="text-bold">'+
gettext('Processing')+'...&nbsp;'+'</span>'+'<span class="cspicon cspicon-spin icon-refresh"></span>'+'<% } %>'+'</div>'+'<% } %>'+'</div>'+'</div>'),initialize:function(options){this.listenTo(this.model,'change',this.render);this.listenTo(this.model,'sync',this.sync);this.email_view=new EmailDownloadRequestView({model:this.model});this.$el.modal({show:false});},sync:function(model,resp,options){if(this.model.get('is_ready')&&!this.model.get('is_locked')){var url=this.model.get('url');download(url);}},render:function(){var html=this.template({request:this.model});this.$el.html(html);if(!this.model.get('is_locked')){this.$('div.modal-body').append(this.email_view.el);this.email_view.delegateEvents();}},show:function(){$('body').append(this.$el);this.model.start_polling();this.$el.modal('show');this.render();},hide:function(event){this.model.stop_polling();this.$el.detach();this.trigger('hide');}});var DownloadView=Backbone.View.extend({events:{'click':'click'},initialize:function(options){this.modal=new csp.DownloadRequestView({model:new csp.DownloadRequestModel({downloads:[options.data]})});},click:function(event){this.modal.show();}});$.fn.download=function(params){return this.each(function(){if(!$(this).data('dl')){var options=_.extend(params||{},{el:$(this)});options['data']=$(this).data('download');$(this).data('dl',new DownloadView(options));}});};var DownloadsView=Backbone.View.extend({events:{'change thead input[type="checkbox"]':'toggle','change tbody input[type="checkbox"]':'update','click button.btn-selected':'submit'},toggle:function(event){var checked=this.$(event.currentTarget).is(':checked');this.$('tbody input[type="checkbox"]').prop('checked',checked).trigger('change');},update:function(event){var not_checked=this.$('tbody input[type="checkbox"]:not(:checked)').length,checked=this.$('tbody input[type="checkbox"]:checked').length;this.$('thead input[type="checkbox"]').prop('checked',(not_checked==0));this.$('thead button').prop('disabled',(checked==0));},submit:function(event){event.preventDefault();var downloads=_.map(this.$('input[data-download]:checked'),function(elem,idx){return $(elem).data('download');});(new csp.DownloadRequestView({model:new csp.DownloadRequestModel({downloads:downloads})})).show();}});$.fn.downloads=function(params){return this.each(function(){if(!$(this).data('dls')){var options=_.extend(params||{},{el:$(this)});$(this).data('dls',new DownloadsView(options));}});};var InfluentialView=Backbone.View.extend({events:{'change input[name="url"]':'change','keyup input[name="url"]':'change','input input[name="url"]':'change','click button.btn-refund':'refund','submit':'submit'},initialize:function(options){this.store();},store:function(){this.initial=this.$('input[name="url"]').val();},change:function(event){var disabled=(this.$('input[name="url"]').val()===this.initial);this.$('button.btn-save').prop('disabled',disabled);},submit:function(event){event.preventDefault();this.store();this.change();$.ajax({data:this.$('form').serialize(),url:jslink({viewname:'download:influential'})});},refund:function(event){if(confirm(gettext('Are you sure?'))){$.ajax({data:this.$('form').serialize(),url:jslink({viewname:'download:refund',}),success:function(data){if(!data.can_refund){$('button.btn-refund').prop('disabled',true);}}});$('tr[data-download_id="'+this.$el.data('download_id')+'"]').remove();}}});$.fn.influential=function(params){return this.each(function(){if(!$(this).data('influential')){var options=_.extend(params||{},{el:$(this)});$(this).data('influential',new InfluentialView(options));}});};})(window.canstockphoto=window.canstockphoto||{},jQuery);;(function(csp,$,undefined){csp.DetailRouter=Backbone.Router.extend({initialize:function(){this.route(new RegExp('^(.+)$'),'all');this.on('route:all',_.bind(this.load,this));Backbone.history.start({hashChange:false,pushState:true,silent:true});},load:function(url){window.location.href='/'+url;}});})(window.canstockphoto=window.canstockphoto||{},jQuery);;(function(csp,$,undefined){var FormView=Backbone.View.extend({events:{'change input[name="display_size"]':'display','click span.view-addons':'toggle','change tbody input':'update','click tbody tr':'select','submit':'submit'},initialize:function(options){this.$('tr td span[data-modal]').on('click',AjaxModalView.onClick);this.update();},toggle:function(event){this.$('span.view-addons').remove();this.$('tbody.addons').show();},select:function(event){if(this.$(event.target).is('input'))return;var $input=this.$(event.currentTarget).find('input:not(:disabled)'),checked=$input.is(':radio')?true:!$input.is(':checked');$input.prop('checked',checked).trigger('change');event.stopPropagation();event.preventDefault();},display:function(event){var mode=this.$('input[name="display_size"]:checked').val();this.$('span.px,span.cm,span.in').hide();this.$('span.'+mode).show();},total:function(){var cash=0,credits=0;this.$('tbody tr input[data-cash]:checked').each(function(idx,elem){credits+=parseFloat($(elem).data('credits'));cash+=parseFloat($(elem).data('cash'));});return{cash:format_price(cash,true),credits:credits};},can_buy_with_subscription:function(){var subscription=csp.user.get('subscription'),data=this.$el.serializeObject();return(!_.has(data,'addons')&&subscription.remaining()>0&&subscription.is_valid(parseInt(data.product)));},can_buy_with_credits:function(){return csp.user.has_credits(this.total().credits);},update:function(){var total=this.total(),has_credits=csp.user.has_credits(1),can_credits_buy=this.can_buy_with_credits(),can_subscription_buy=this.can_buy_with_subscription(),amount=(has_credits)?total.credits:total.cash;this.$('div.purchase-total').toggleClass('danger',(has_credits&&!can_credits_buy));this.$('div.purchase-terms').toggle(can_credits_buy||can_subscription_buy);this.$('div.purchase-total').toggle(!can_subscription_buy);this.$('tbody tr.active').removeClass('active');this.$('tbody tr input:checked').closest('tr').addClass('active');var text=interpolate(gettext('Total: %(amount)s'),{amount:'<span>'+amount+'</span>'},true);this.$('div.purchase-total').html(text);},submit:function(event){if(!this.can_buy_with_subscription()&&!this.can_buy_with_credits()){this.$('button.btn-download').prop('disabled',true);return true;}
if(!this.$('input[name="accept"]').is(':checked')){toastr.error(gettext('You must agree to our license terms in order to download this file.'),gettext('Error'),{timeOut:5000});}else{this.$('button.btn-download').prop('disabled',true);$.ajax({success:_.bind(this.success,this),data:this.$el.serializeObject(),url:location.href,method:'POST'});}
return false;},success:function(data){if(data.success){(new csp.DownloadRequestView({model:new csp.DownloadRequestModel({downloads:[data]})})).once('hide',function(){this.$('button.btn-download').prop('disabled',false);},this).show();}else{this.$('button.btn-download').prop('disabled',false);toastr.error(gettext('We are having trouble with your last request.'),gettext('Error'),{timeOut:5000});}}});var Detail=Backbone.View.extend({initialize:function(options){this.options=options;this._resize=_.throttle(_.bind(this.resize,this),500);$(window).on('resize',this._resize);if(this.options.enable_router){new csp.DetailRouter();}
this.form=new FormView({el:this.$('form.purchase')});this.$('div.relatedreel img').bannerhide({applyHeightContainer:true,maxWidth:190,center:true,height:129});this.$('div.relatedreel').css('height',(129+(47+2))).photoreel({container:'div.relatedscroll'});this.$('div.relatedgridreel').photoreel({container:'div.relatedscroll',vertical:true});this.$('div.relatedgridreel ul').on('grid:resized',_.bind(function(){this.$('div.relatedgridreel').data('photoreel').update();},this));if($.fn.grid){this.$('div.relatedgridreel ul').grid({process_cell:function(width,height){this.$el.parent().parent().css({height:(height+35),width:width});}}).trigger('grid:resize');}
if(this.options.zoom_sizes.length>0){this.$('div.zoom-view').zoom({message:gettext('Sorry, you have reached your zoom limit for this image. Thank you.'),sizes:this.options.zoom_sizes,id:this.options.image_id});}
this.$('label span[data-modal]').on('click',AjaxModalView.onClick);this.$('li[title]').tooltip({container:'body'});this.resize();},adjust:function(elem,height){var container_height=(height+(47+2));if(this.$(elem).height()!==container_height){this.$(elem).css('height',container_height).find('img').bannerhide({applyHeightContainer:true,maxWidth:(height*1.5),height:height,center:true});this.$(elem).data('photoreel').update();}},resize:function(event){var width=$(window).width();this.form.$el.appendTo((width<977)?'#purchase_mobile':'#purchase_full');this.$('div.zoom-view').find('img[data-ratio]').bannerhide({maxWidth:Math.min((width-30),450),bannerVertical:false,height:450});if(this.$('div.image-detail2-main').length>0){this.$('div.image-related.floater').toggle(width>1199);var height=this.$('div.image-detail2-main').height();this.$('div.relatedgridreel').height((height-11));this.$('div.relatedreel').each(_.bind(function(idx,elem){if(width>1200){this.adjust(elem,190);}else if(width>992){this.adjust(elem,150);}else{this.adjust(elem,129);}},this));}},remove:function(){this.$('li[title]').tooltip('destroy');$(window).off('resize',this._resize);Backbone.View.prototype.remove.apply(this,arguments);}});$.fn.detail=function(options,enable_router){return this.each(function(){if(!$(this).data('detail')){var params=_.extend(options||{},{enable_router:enable_router,el:$(this)});$(this).data('detail',new Detail(params));}});};})(window.canstockphoto=window.canstockphoto||{},jQuery);