USE [VoiceRecord3]
GO

/****** Object:  StoredProcedure [dbo].[Sel_Dispostion]    Script Date: 01/05/2015 15:01:02 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO



CREATE PROCEDURE [dbo].[Sel_Dispostion] 

AS
	select * from dbo.Disposition where id>=64


GO


