USE [VoiceRecord3]
GO

/****** Object:  StoredProcedure [dbo].[Sel_Service]    Script Date: 01/05/2015 15:01:36 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO



CREATE PROCEDURE [dbo].[Sel_Service] 
@WorkGroup int
AS
	select * from dbo.Service where id !=0


GO


