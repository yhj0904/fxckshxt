/**
 * @license Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	config.toolbar = [
	              	['Source','-','NewPage','-'],
	              	['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print'],
	              	['Undo','Redo','-'],
	              	['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
	              	['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],'/',
	              	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],['Link','Unlink'],
	              	['Table','SpecialChar','PageBreak'],'/',
	              	['Styles','Format','Font','FontSize'],['TextColor','BGColor'],['Maximize', 'ShowBlocks']];
	
	config.toolbar_ImageUpload = [
						      	['Source','-','NewPage','-'],
						      	['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print'],
						      	['Undo','Redo','-'],
						      	['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
						      	['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],'/',
						      	['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],['Link','Unlink'],
						      	['Image','Table','SpecialChar','PageBreak'],'/',
						      	['Styles','Format','Font','FontSize'],['TextColor','BGColor'],['Maximize', 'ShowBlocks']];
	
	config.toolbar_IndexPage = [['Source'],['Maximize', 'ShowBlocks']];
	config.uiColor = '#EEEEEE';		//ui 색상
	config.width = "auto";
    config.height = "500px";
	config.contentsCss = ['/css/common/common.css'];	//홈페이지에서 사용하는 Css 파일 인클루드
	config.font_defaultLabel = 'NotoSansKR';	
	config.font_names='NotoSansKR/NotoSansKR;Gulim/Gulim;Dotum/Dotum;Batang/Batang;Gungsuh/Gungsuh/Arial/Arial;Tahoma/Tahoma;Verdana/Verdana';
	config.fontSize_defaultLabel = '14px';
	config.fontSize_sizes='8/8px;9/9px;10/10px;11/11px;12/12px;14/14px;16/16px;18/18px;20/20px;22/22px;24/24px;26/26px;28/28px;36/36px;48/48px;';
	config.enterMode =CKEDITOR.ENTER_BR;	//엔터키 입력시 br 태그 변경
	config.allowedContent = true;			// 기본적인 html이 필터링으로 지워지는데 필터링을 하지 않는다.
	config.toolbarCanCollapse = true;		//툴바가 접히는 기능을 넣을때 사용합니다.
	config.docType = "<!DOCTYPE html>";		//문서타입 설정
};
